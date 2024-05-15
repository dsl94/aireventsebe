package com.airevents.repository;

import com.airevents.entity.RaceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceTypeRepository extends JpaRepository<RaceType, Long> {
    RaceType findByNameIgnoreCase(String name);
}
