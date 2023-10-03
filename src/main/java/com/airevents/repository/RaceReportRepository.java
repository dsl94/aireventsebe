package com.airevents.repository;

import com.airevents.entity.RaceReport;
import com.airevents.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceReportRepository extends JpaRepository<RaceReport, Long> {

    List<RaceReport> findAllByUserOrderByDateOfRaceAsc(User user);
    List<RaceReport> findAllByOrderByDateOfRaceAsc();
}
