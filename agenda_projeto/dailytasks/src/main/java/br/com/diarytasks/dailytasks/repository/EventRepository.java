package br.com.diarytasks.dailytasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.diarytasks.dailytasks.model.Day;

public interface EventRepository extends JpaRepository<Day, Integer>{
}