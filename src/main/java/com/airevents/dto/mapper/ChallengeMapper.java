package com.airevents.dto.mapper;

import com.airevents.dto.request.ChallengeRequest;
import com.airevents.dto.request.RaceRequest;
import com.airevents.dto.response.ChallengeResponse;
import com.airevents.dto.response.RaceResponse;
import com.airevents.dto.response.UserRaceResponse;
import com.airevents.entity.Challenge;
import com.airevents.entity.Race;
import com.airevents.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChallengeMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter REQUEST_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static Challenge requestToEntity(ChallengeRequest request) {
        Challenge challenge = new Challenge();
        challenge.setTitle(request.getTitle());
        challenge.setStartDate(LocalDate.parse(request.getStartDate(), REQUEST_DATE_FORMAT).atStartOfDay());
        challenge.setEndDate(LocalDate.parse(request.getEndDate(), REQUEST_DATE_FORMAT).atTime(LocalTime.of(23,59,59)));

        return challenge;
    }

    public static ChallengeResponse entityToResponse(Challenge entity) {
        return new ChallengeResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getStartDate().format(DATE_FORMATTER),
                entity.getEndDate().format(DATE_FORMATTER),
                entity.getLastSync() == null ? "Do sada nije sinhronizovano" : entity.getLastSync().format(DATE_FORMATTER),
                getUsers(entity.getUsers())
        );
    }

    public static List<UserRaceResponse> getUsers(Set<User> users) {
        return users.stream().map(u -> new UserRaceResponse(u.getId(), u.getFullName())).collect(Collectors.toList());
    }
}
