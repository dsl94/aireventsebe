package com.airevents.dto.mapper;

import com.airevents.dto.request.CreateRaceReportRequest;
import com.airevents.dto.response.RaceReportResponse;
import com.airevents.dto.response.UserRaceResponse;
import com.airevents.entity.RaceReport;
import com.airevents.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RaceReportMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter REQUEST_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static RaceReport requestToEntity(CreateRaceReportRequest request) {
        RaceReport race = new RaceReport();
        race.setRaceTitle(request.getTitle());
        race.setDistance(request.getDistance());
        race.setInfo(request.getInfo());
        race.setDateOfRace(LocalDate.parse(request.getDate(), REQUEST_DATE_FORMAT).atStartOfDay());

        return race;
    }

    public static RaceReportResponse entityToResponse(RaceReport entity) {
        return new RaceReportResponse(
                entity.getId(),
                entity.getRaceTitle(),
                entity.getDateOfRace().format(DATE_FORMATTER),
                entity.getDistance(),
                entity.getInfo(),
                getUser(entity.getUser())
        );
    }

    public static UserRaceResponse getUser(User user) {
        return  new UserRaceResponse(user.getId(), user.getFullName());
    }
}
