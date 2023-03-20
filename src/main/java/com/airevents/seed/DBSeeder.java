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

    @Value("classpath:sql-data/airports.dat")
    Resource airportsFile;

    @Value("classpath:sql-data/planes.dat")
    Resource aircraftFile;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @EventListener
    public void seed(ContextRefreshedEvent event) throws IOException {
        seedRolesTable();
        seedUsersTable();
        seedAirports();
        seedAirplanes();
    }

    private void seedRolesTable() {
        if (roleRepository.findAll().size() == 0) {
            logger.info("Seeding roles");
            Role admin = new Role();
            admin.setName("Admin");
            admin.setRole(RolesConstants.ROLE_ADMIN.name());
            roleRepository.save(admin);

            Role user = new Role();
            user.setName("User");
            user.setRole(RolesConstants.ROLE_USER.name());
            roleRepository.save(user);

            Role systemAdmin = new Role();
            systemAdmin.setName("Super admin");
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

    private void seedAirports() throws IOException {
        if (airportRepository.findAll().size() == 0) {
            logger.info("Seeding airports");

            Reader in = new InputStreamReader(getClass().getResourceAsStream("/sql-data/airports.dat"));

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

            List<Airport> airportsForSave = new ArrayList<>();

            for (CSVRecord record : records) {
                String name = record.get(1).replace("'", "");
                String city = record.get(2).replace("'", "");
                String country = record.get(3).replace("'", "");
                String iata = record.get(4).replace("'", "");
                String icao = record.get(5).replace("'", "");
                String latitude = record.get(6).replace("'", "");
                String longitude = record.get(7).replace("'", "");


//                if (icao.startsWith("L") || icao.startsWith("E") || icao.startsWith("UU")) {
                    Airport airport = new Airport();
                    airport.setName(name);
                    airport.setCity(city);
                    airport.setCountry(country);
                    airport.setIata(iata);
                    airport.setIcao(icao);
                    airport.setLatitude(Double.valueOf(latitude));
                    airport.setLongitude(Double.valueOf(longitude));

                    airportsForSave.add(airport);
//                    airportRepository.saveAndFlush(airport);
//                }
            }

            airportRepository.saveAll(airportsForSave);
        }
    }

    public void seedAirplanes() throws IOException {
        if (aircraftRepository.findAll().size() == 0) {
            logger.info("Seeding aircrafts");
            Reader in = new InputStreamReader(getClass().getResourceAsStream("/sql-data/planes.dat"));
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

            List<Aircraft> airplanesForSave = new ArrayList<>();

            for (CSVRecord record : records) {
                String name = record.get(0).replace("'", "");
                String icao = record.get(2).replace("'", "");
                String price = record.get(3).replace("'", "");
                String maxPax = record.get(4).replace("'", "");

                Aircraft aircraft = new Aircraft();
                aircraft.setName(name);
                aircraft.setIcao(icao);
                aircraft.setPrice(Long.valueOf(price));
                aircraft.setMaxPassengers(Long.valueOf(maxPax));
                aircraft.setMaxCargo(0L);
                aircraft.setCargo(false);

                airplanesForSave.add(aircraft);
//                aircraftRepository.saveAndFlush(aircraft);
            }

            aircraftRepository.saveAll(airplanesForSave);
        }
    }
}
