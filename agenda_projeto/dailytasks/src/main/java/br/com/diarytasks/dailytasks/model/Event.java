package br.com.diarytasks.dailytasks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Event{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
     
    @Column(length = nameMaxLength, nullable = false)
    private String name;

    private short startMinute;
    private short endMinute;
    private int color;
    private static final byte nameMaxLength = 50; 

    protected Event(){} 

    public Event(String name, short startMinute, short endMinute, int color){
        this.name = name;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
        this.color = color;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public short getStartMinute(){
        return startMinute;
    }
    public short getEndMinute(){
        return endMinute;
    }
    public static byte getNameMaxLength(){
        return nameMaxLength;
    }
    public int getColor(){
        return color;
    }
}
