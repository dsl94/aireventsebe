package com.airevents.repository;

import com.airevents.entity.AircraftMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AircraftMappingRepository extends JpaRepository<AircraftMapping, Long> {

    Optional<AircraftMapping> findBySimKeyIgnoreCase(String simKey);
}
