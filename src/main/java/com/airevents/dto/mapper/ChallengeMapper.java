package com.airevents.dto.mapper;

import com.airevents.dto.request.ChallengeRequest;
import com.airevents.dto.request.RaceRequest;
import com.airevents.dto.response.ChallengeResponse;
import com.airevents.dto.response.RaceResponse;
import com.airevents.dto.response.UserChallengeResponse;
import com.airevents.dto.response.UserRaceResponse;
import com.airevents.entity.Challenge;
import com.airevents.entity.Race;
import com.airevents.entity.User;
import com.airevents.entity.UserChallenge;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChallengeMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
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
                entity.getLastSync() == null ? "Do sada nije sinhronizovano" : entity.getLastSync().format(DATE_TIME_FORMATTER),
                getUsers(entity.getUserChallenges())
        );
    }

    public static List<UserChallengeResponse> getUsers(Set<UserChallenge> users) {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory factory = TypeFactory.defaultInstance();
        MapType mapType = factory.constructMapType(HashMap.class, String.class, Double.class);
        List<UserChallengeResponse> list = new ArrayList<>();
        for (UserChallenge u : users) {
            UserChallengeResponse userChallengeResponse = null;
            try {
                userChallengeResponse = new UserChallengeResponse(u.getUser().getId(), u.getUser().getFullName(), u.getDistance(), "M".equals(u.getUser().getGender()), u.getPerMonth() != null ? mapper.readValue(u.getPerMonth(), mapType) : null);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            list.add(userChallengeResponse);
        }
        return list;
    }
}
