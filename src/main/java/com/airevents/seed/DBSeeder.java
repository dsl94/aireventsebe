package com.airevents.seed;

import com.airevents.entity.*;
import com.airevents.repository.*;
import com.airevents.security.RolesConstants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DBSeeder {

    private Logger logger = LoggerFactory.getLogger(DBSeeder.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void seed(ContextRefreshedEvent event) throws IOException {
        seedRolesTable();
        seedUsersTable();
    }

    private void seedRolesTable() {
        if (roleRepository.findAll().size() == 0) {
            logger.info("Seeding roles");
            Role admin = new Role();
            admin.setName("Administrator");
            admin.setRole(RolesConstants.ROLE_ADMIN.name());
            roleRepository.save(admin);

            Role user = new Role();
            user.setName("Korisnik");
            user.setRole(RolesConstants.ROLE_USER.name());
            roleRepository.save(user);

            Role systemAdmin = new Role();
            systemAdmin.setName("Sistemski administrator");
            systemAdmin.setRole(RolesConstants.ROLE_SYSTEM_ADMIN.name());
            roleRepository.save(systemAdmin);
        }
    }

    private void seedUsersTable() {
        if (userRepository.findAll().size() == 0) {
            logger.info("Seeding users");
            Role systemAdminRole = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_SYSTEM_ADMIN.name());

            User systemAdmin = new User();
            systemAdmin.setFullName("System Admin");
            systemAdmin.setEmail("systemadmin@mail.com");
            systemAdmin.setUsername("systemAdmin");
            systemAdmin.setPassword(passwordEncoder.encode("systemadmin"));
            systemAdmin.setActive(true);
            systemAdmin.setRoles(Collections.singleton(systemAdminRole));
            userRepository.save(systemAdmin);
        }
    }
}
