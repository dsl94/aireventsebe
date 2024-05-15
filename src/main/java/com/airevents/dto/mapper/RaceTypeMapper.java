package com.airevents.dto.mapper;

import com.airevents.dto.response.RaceDistanceResponse;
import com.airevents.dto.response.RaceTypeWithDistancesResponse;
import com.airevents.entity.RaceDistance;
import com.airevents.entity.RaceType;

import java.util.stream.Collectors;

public class RaceTypeMapper {

    public static RaceTypeWithDistancesResponse mapToRaceTypeResponse(RaceType raceType) {
        return new RaceTypeWithDistancesResponse(
                raceType.getId(),
                raceType.getName(),
                raceType.getDistances().stream().map(RaceTypeMapper::mapDistanceToResponse).collect(Collectors.toList())
        );
    }

    public static RaceDistanceResponse mapDistanceToResponse(RaceDistance raceDistance) {
        return new RaceDistanceResponse(raceDistance.getId(), raceDistance.getDistance());
    }
}
