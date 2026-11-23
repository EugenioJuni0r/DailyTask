package br.com.diarytasks.dailytasks.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Day{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "day_id")
    private List<Event> events = new ArrayList<>();
    
    private static final String[] namesWeek = {"dom", "seg", "ter", "quar", "quin", "sex", "sab"};
    private static final byte firstDayOfWeek = 5;
    
    private boolean[] minutes = new boolean[1440]; 
    private short dayIndex; 
    private String weekDay; 

    protected Day() {} 

    public Day(short dayIndex){
        this.dayIndex = dayIndex;
        this.weekDay = namesWeek[(firstDayOfWeek + dayIndex) % 7];
    }

    public short getDayIndex(){
        return dayIndex;
    }
    public String getWeekDay(){
        return weekDay;
    }
    public boolean getMinute(int startMinute, int endMinute){
        if(startMinute >= 0 && endMinute <= 1440 && startMinute < endMinute){
            for(int i = startMinute; i < endMinute; i++){
                    if(minutes[i]){
                        return true; 
                    }
                }
        }
        return false; 
    }
    public void setMinute(int startMinute, int endMinute, boolean value){
        if(startMinute >= 0 && endMinute <= 1440 && startMinute < endMinute){
            for(int i = startMinute; i < endMinute; i++){
                minutes[i] = value; 
            }
        }
    }
    
    public void addEvent(Event event){
        events.add(event);
        setMinute(event.getStartMinute(), event.getEndMinute(), true);
    }
    public void removeEvent(Event event){
        if(events.remove(event)){
            setMinute(event.getStartMinute(), event.getEndMinute(), false);
        }
    }
    public void updateEvent(Event oldEvent, Event newEvent){
        int pos = events.indexOf(oldEvent);
        if (pos != -1) {
            setMinute(oldEvent.getStartMinute(), oldEvent.getEndMinute(), false);
            events.set(pos, newEvent);
            setMinute(newEvent.getStartMinute(), newEvent.getEndMinute(), true);
        }
    }
    public List<Event> getEvents(){
        return events;
    }
}
