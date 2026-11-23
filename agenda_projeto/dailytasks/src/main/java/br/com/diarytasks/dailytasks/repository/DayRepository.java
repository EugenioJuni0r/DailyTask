package br.com.diarytasks.dailytasks.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.diarytasks.dailytasks.model.Day;

public interface DayRepository extends JpaRepository<Day, Integer>{

    @Query("select d from Diary di join di.days d where di.id = :diaryId and d.dayIndex = :dayIndex")
    Optional<Day> findByDiaryAndIndex(@Param("diaryId") int diaryId, @Param("dayIndex") short dayIndex);

    @Query("select d from Diary di join di.days d where di.id = :diaryId and d.dayIndex between :startIndex and :endIndex order by d.dayIndex")
    List<Day> findRange(@Param("diaryId") int diaryId, @Param("startIndex") short startIndex, @Param("endIndex") short endIndex);
}