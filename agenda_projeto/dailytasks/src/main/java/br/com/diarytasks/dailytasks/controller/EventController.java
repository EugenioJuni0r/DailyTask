package br.com.diarytasks.dailytasks.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.diarytasks.dailytasks.dto.EventRequest;
import br.com.diarytasks.dailytasks.model.Event;
import br.com.diarytasks.dailytasks.service.DiaryService;

@RestController
@RequestMapping("/diaries/{diaryId}/days/{dayIndex}/events")
public class EventController{

    private final DiaryService diaryService;

    public EventController(DiaryService diaryService){
        this.diaryService = diaryService;
    }

    @GetMapping
    public List<Event> list(@PathVariable int diaryId, @PathVariable short dayIndex){
        return diaryService.listEvents(diaryId, dayIndex);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@PathVariable int diaryId,
                    @PathVariable short dayIndex,
                    @RequestBody EventRequest request){
        diaryService.addEvent(diaryId, dayIndex, request.name(),
                request.startMinute(), request.endMinute(), request.color(), request.repeatWeekly());
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int diaryId,
                       @PathVariable short dayIndex,
                       @PathVariable int eventId){
        diaryService.removeEvent(diaryId, dayIndex, eventId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clear(@PathVariable int diaryId, @PathVariable short dayIndex){
        diaryService.clearDay(diaryId, dayIndex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException e){
        return Map.of("erro", e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflict(IllegalStateException e){
        return Map.of("erro", e.getMessage());
    }
}