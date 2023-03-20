package com.airevents.repository;

import com.airevents.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleIgnoreCase(String role);
}
