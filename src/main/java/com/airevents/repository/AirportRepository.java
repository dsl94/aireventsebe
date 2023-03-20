package com.airevents.repository;

import com.airevents.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    List<Airport> findByLatitudeGreaterThanEqualAndLatitudeLessThanEqualAndLongitudeGreaterThanEqualAndLongitudeLessThanEqual(Double minLat, Double maxLat, Double minLon, Double maxLon);
    Airport findByIcaoIgnoreCase(String icao);
}
