package com.airevents.repository;

import com.airevents.entity.Challenge;
import com.airevents.entity.User;
import com.airevents.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    List<UserChallenge> findByUser(User user);
    List<UserChallenge> findByChallenge(Challenge challenge);
    Optional<UserChallenge> findByChallengeAndUser(Challenge challenge, User user);
}
