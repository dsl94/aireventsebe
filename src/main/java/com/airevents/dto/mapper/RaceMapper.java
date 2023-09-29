package com.airevents.dto.mapper;

import com.airevents.dto.request.CreateUserRequest;
import com.airevents.dto.request.RaceRequest;
import com.airevents.dto.response.RaceResponse;
import com.airevents.dto.response.UserRaceResponse;
import com.airevents.dto.response.UserResponse;
import com.airevents.entity.Race;
import com.airevents.entity.Role;
import com.airevents.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RaceMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter REQUEST_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static Race requestToEntity(RaceRequest request) {
        Race race = new Race();
        race.setRaceTitle(request.getTitle());
        race.setDistances(request.getDistances());
        race.setDateOfRace(LocalDate.parse(request.getDate(), REQUEST_DATE_FORMAT).atStartOfDay());

        return race;
    }

    public static RaceResponse entityToResponse(Race entity) {
        return new RaceResponse(
                entity.getId(),
                entity.getRaceTitle(),
                entity.getDateOfRace().format(DATE_FORMATTER),
                entity.getDistances(),
                getUsers(entity.getUsers())
        );
    }

    public static List<UserRaceResponse> getUsers(Set<User> users) {
        return users.stream().map(u -> new UserRaceResponse(u.getId(), u.getFullName())).collect(Collectors.toList());
    }
}
