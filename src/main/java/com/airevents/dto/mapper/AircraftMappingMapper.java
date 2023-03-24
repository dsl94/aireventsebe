package com.airevents.dto.mapper;

import com.airevents.dto.request.AircraftMappingRequest;
import com.airevents.dto.response.AircraftMappingResponse;
import com.airevents.entity.AircraftMapping;

public class AircraftMappingMapper {

    public static AircraftMappingResponse mapToResponse(AircraftMapping entity) {
        return new AircraftMappingResponse(
                entity.getId(),
                entity.getSimKey(),
                entity.getIcao()
        );
    }

    public static AircraftMapping mapToEntity(AircraftMappingRequest request) {
        AircraftMapping entity = new AircraftMapping();
        entity.setSimKey(request.getSimKey());
        entity.setIcao(request.getIcao());

        return entity;
    }
}
