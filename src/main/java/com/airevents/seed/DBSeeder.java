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
    private RaceTypeRepository raceTypeRepository;
    @Autowired
    private RaceDistanceRepository raceDistanceRepository;
    @Autowired
    private RaceV2Repository raceV2Repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void seed(ContextRefreshedEvent event) throws IOException {
        seedRolesTable();
        seedUsersTable();
        seedRaceTypes();
        seedDistances();
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

            Role guest = new Role();
            guest.setName("Gost");
            guest.setRole(RolesConstants.ROLE_USER.name());
            roleRepository.save(guest);

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
            systemAdmin.setFullName("Djordje Andjelkovic");
            systemAdmin.setEmail("andjelkovicdjordje@gmail.com");
            systemAdmin.setUsername("andjelkovicdjordje@gmail.com");
            systemAdmin.setPassword(passwordEncoder.encode("bythefly"));
            systemAdmin.setActive(true);
            systemAdmin.setRoles(Collections.singleton(systemAdminRole));
            userRepository.save(systemAdmin);
        }
    }

    private void seedRaceTypes() {
        if (raceTypeRepository.findAll().size() == 0) {
            logger.info("Seeding race types");

            RaceType road = new RaceType();
            road.setName("Drumsko trčanje");
            raceTypeRepository.saveAndFlush(road);

            RaceType trail = new RaceType();
            trail.setName("Trail trčanje");
            raceTypeRepository.saveAndFlush(trail);

            RaceType triathlon = new RaceType();
            triathlon.setName("Trijatlon");
            raceTypeRepository.saveAndFlush(triathlon);

            RaceType swim = new RaceType();
            swim.setName("Plivanje");
            raceTypeRepository.saveAndFlush(swim);

            RaceType bike = new RaceType();
            bike.setName("Biciklizam");
            raceTypeRepository.saveAndFlush(bike);
        }
    }

    private void seedDistances() {
        if (raceDistanceRepository.findAll().size() == 0) {
            logger.info("Seeding distances");
            RaceType road = raceTypeRepository.findByNameIgnoreCase("Drumsko trčanje");
            RaceType trail = raceTypeRepository.findByNameIgnoreCase("Trail trčanje");
            RaceType triathlon = raceTypeRepository.findByNameIgnoreCase("Trijatlon");
            RaceType swim = raceTypeRepository.findByNameIgnoreCase("Plivanje");
            RaceType bike = raceTypeRepository.findByNameIgnoreCase("Biciklizam");

            // Road
            RaceDistance road5 = new RaceDistance();
            road5.setDistance("5km");
            road5.setRaceType(road);
            raceDistanceRepository.saveAndFlush(road5);

            RaceDistance road7 = new RaceDistance();
            road7.setDistance("7km");
            road7.setRaceType(road);
            raceDistanceRepository.saveAndFlush(road7);

            RaceDistance road10 = new RaceDistance();
            road10.setDistance("10km");
            road10.setRaceType(road);
            raceDistanceRepository.saveAndFlush(road10);

            RaceDistance hm = new RaceDistance();
            hm.setDistance("Polumaraton");
            hm.setRaceType(road);
            raceDistanceRepository.saveAndFlush(hm);

            RaceDistance m = new RaceDistance();
            m.setDistance("Maraton");
            m.setRaceType(road);
            raceDistanceRepository.saveAndFlush(m);

            // Trail
            RaceDistance tc = new RaceDistance();
            tc.setDistance("Custom");
            tc.setRaceType(trail);
            raceDistanceRepository.saveAndFlush(tc);

            // Triathlon
            RaceDistance sprint = new RaceDistance();
            sprint.setDistance("Sprint trijatlon");
            sprint.setRaceType(triathlon);
            raceDistanceRepository.saveAndFlush(sprint);

            RaceDistance ot = new RaceDistance();
            ot.setDistance("Olimpijski trijatlon");
            ot.setRaceType(triathlon);
            raceDistanceRepository.saveAndFlush(ot);

            RaceDistance half = new RaceDistance();
            half.setDistance("Poludistanca");
            half.setRaceType(triathlon);
            raceDistanceRepository.saveAndFlush(half);

            RaceDistance full = new RaceDistance();
            full.setDistance("Puna distanca");
            full.setRaceType(triathlon);
            raceDistanceRepository.saveAndFlush(full);

            // Bike
            RaceDistance bc = new RaceDistance();
            bc.setDistance("Custom");
            bc.setRaceType(bike);
            raceDistanceRepository.saveAndFlush(bc);

            // Swim
            RaceDistance swim1 = new RaceDistance();
            swim1.setDistance("1km");
            swim1.setRaceType(swim);
            raceDistanceRepository.saveAndFlush(swim1);

            RaceDistance swim3 = new RaceDistance();
            swim3.setDistance("3km");
            swim3.setRaceType(swim);
            raceDistanceRepository.saveAndFlush(swim3);

            RaceDistance swim5 = new RaceDistance();
            swim5.setDistance("5km");
            swim5.setRaceType(swim);
            raceDistanceRepository.saveAndFlush(swim5);
        }
    }
}
