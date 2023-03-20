package com.airevents.repository;

import com.airevents.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.airevents.entity.Flight;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.user.username = :username")
    List<Flight> findByUsername(@Param("username") String username);

    List<Flight> findAllByOrderByEndTimeDesc();

    List<Flight> findTop5ByUserOrderByEndTimeDesc(User user);
}
