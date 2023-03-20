package com.airevents.repository;

import com.airevents.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameIgnoreCase(String username);
    User findByEmailIgnoreCase(String email);
}
