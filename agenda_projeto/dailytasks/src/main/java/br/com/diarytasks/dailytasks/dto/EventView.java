package br.com.diarytasks.dailytasks.dto;

public record EventView(short dayIndex, int id, String name, short startMinute, short endMinute, int color){
}