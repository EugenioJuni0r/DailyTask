package br.com.diarytasks.dailytasks.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Diary{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "diary_id")
    @OrderBy("dayIndex")
    private List<Day> days = new ArrayList<>();

    public Diary() {
        for (short i = 0; i < 365; i++) {
            days.add(new Day(i));
        }
    }
    
    public int getId() {
        return id;
    }
    public List<Day> getDays() {
        return days;
    }
}