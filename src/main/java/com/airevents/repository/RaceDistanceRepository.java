package com.airevents.repository;

import com.airevents.entity.RaceDistance;
import com.airevents.entity.RaceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceDistanceRepository extends JpaRepository<RaceDistance, Long> {
}
