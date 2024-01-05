package com.airevents.repository;

import com.airevents.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RaceRepository extends JpaRepository<Race, Long> {
    List<Race> findAllByDateOfRaceAfterOrderByDateOfRaceAsc(LocalDateTime dateTime);
}
