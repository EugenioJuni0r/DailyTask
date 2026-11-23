package br.com.diarytasks.dailytasks.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.diarytasks.dailytasks.model.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Integer>{

    Optional<Diary> findFirstByOrderByIdAsc();
}