package com.airevents.repository;

import com.airevents.entity.Challenge;
import com.airevents.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findAllByOrderByStartDateDesc();
}
