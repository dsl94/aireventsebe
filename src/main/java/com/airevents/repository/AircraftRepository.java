package com.airevents.repository;

import com.airevents.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    List<Aircraft> findByIcaoIgnoreCase(String icao);
    Aircraft findByIcaoIgnoreCaseAndCargo(String icao, boolean cargo);
    List<Aircraft> findByPriceIsGreaterThan(Long minPrice);
    List<Aircraft> findAllByOrderByPriceDesc();
}
