package br.com.diarytasks.dailytasks.controller;

import java.util.Map;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.diarytasks.dailytasks.dto.EventView;
import br.com.diarytasks.dailytasks.service.DiaryService;

@RestController
@RequestMapping("/diaries")
public class DiaryController{
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService){
        this.diaryService = diaryService;
    }
    @GetMapping("/{diaryId}/events")
    public List<EventView> range(@PathVariable int diaryId,
                             @RequestParam short from,
                             @RequestParam short to) {
    return diaryService.listEventsBetween(diaryId, from, to);
}
    @GetMapping("/current")
    public Map<String, Integer> current(){
        return Map.of("id", diaryService.getOrCreateDiary());
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Integer> create(){
        int id = diaryService.createDiary();
        return Map.of("id", id);
    }
} 
