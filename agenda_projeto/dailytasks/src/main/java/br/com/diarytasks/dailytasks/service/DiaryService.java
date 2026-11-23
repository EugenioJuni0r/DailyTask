package br.com.diarytasks.dailytasks.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.diarytasks.dailytasks.dto.EventView;
import br.com.diarytasks.dailytasks.model.Day;
import br.com.diarytasks.dailytasks.model.Diary;
import br.com.diarytasks.dailytasks.model.Event;
import br.com.diarytasks.dailytasks.repository.DayRepository;
import br.com.diarytasks.dailytasks.repository.DiaryRepository;

@Service
public class DiaryService{

    private static final int COLOR_COUNT = 6;

    private final DiaryRepository diaryRepository;
    private final DayRepository dayRepository;

    public DiaryService(DiaryRepository diaryRepository, DayRepository dayRepository){
        this.diaryRepository = diaryRepository;
        this.dayRepository = dayRepository;
    }

    @Transactional
    public int getOrCreateDiary(){
        return diaryRepository.findFirstByOrderByIdAsc()
                .map(Diary::getId)
                .orElseGet(this::createDiary);
    }

    public int createDiary(){
        Diary diary = diaryRepository.save(new Diary());
        return diary.getId();
    }

    @Transactional
    public void addEvent(int diaryId, short dayIndex, String name, short start, short end,
                         int color, boolean repeatWeekly){

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("O nome do evento não pode ser vazio");
        }

        if (name.length() > Event.getNameMaxLength()){
            throw new IllegalArgumentException(
                    "O nome do evento pode ter no máximo " + Event.getNameMaxLength() + " caracteres");
        }

        if (start < 0 || end > 1440 || start >= end){
            throw new IllegalArgumentException("Horário inválido");
        }

        if (color < 0 || color >= COLOR_COUNT) {
            throw new IllegalArgumentException("Cor inválida");
        }

        // dias que vão receber o evento: só o escolhido, ou ele e todos os mesmos dias da semana até o fim do ano
        int last = repeatWeekly ? 364 : dayIndex;
        List<Day> days = new ArrayList<>();
        for (int i = dayIndex; i <= last; i += 7){
            days.add(findDay(diaryId, (short) i));
        }

        // confere todos antes de salvar: ou entra em todos os dias, ou em nenhum
        for (Day day : days){
            if (day.getMinute(start, end)) {
                throw new IllegalStateException(days.size() == 1
                        ? "Já existe um evento nesse horário"
                        : "Já existe um evento nesse horário em " + dateOf(day));
            }
        }

        for (Day day : days){
            day.addEvent(new Event(name, start, end, color));
            dayRepository.save(day);
        }
    }

    @Transactional(readOnly = true)
    public List<Event> listEvents(int diaryId, short dayIndex){
        Day day = findDay(diaryId, dayIndex);
        return new ArrayList<>(day.getEvents());
    }

    @Transactional(readOnly = true)
    public List<EventView> listEventsBetween(int diaryId, short from, short to){
        List<EventView> result = new ArrayList<>();
        for (Day day : dayRepository.findRange(diaryId, from, to)) {
            for (Event e : day.getEvents()) {
                result.add(new EventView(day.getDayIndex(), e.getId(), e.getName(),
                        e.getStartMinute(), e.getEndMinute(), e.getColor()));
            }
        }
        result.sort(Comparator.comparingInt(EventView::dayIndex)
                .thenComparingInt(EventView::startMinute));
        return result;
    }

    @Transactional
    public void removeEvent(int diaryId, short dayIndex, int eventId){
        Day day = findDay(diaryId, dayIndex);

        Event event = day.getEvents().stream()
                .filter(e -> e.getId() == eventId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        day.removeEvent(event);
        dayRepository.save(day);
    }

    @Transactional
    public void clearDay(int diaryId, short dayIndex){
        Day day = findDay(diaryId, dayIndex);
        for (Event event : new ArrayList<>(day.getEvents())){
            day.removeEvent(event);
        }
        dayRepository.save(day);
    }

    private Day findDay(int diaryId, short dayIndex){
        return dayRepository.findByDiaryAndIndex(diaryId, dayIndex)
                .orElseThrow(() -> new IllegalArgumentException("Dia não encontrado"));
    }

    private String dateOf(Day day){
        return LocalDate.ofYearDay(2027, day.getDayIndex() + 1).format(DateTimeFormatter.ofPattern("dd/MM"));
    }
}