package br.com.diarytasks.dailytasks.dto;

public record EventRequest(String name, short startMinute, short endMinute, int color, boolean repeatWeekly){
}