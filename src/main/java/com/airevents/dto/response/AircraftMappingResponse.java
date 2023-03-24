package com.airevents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AircraftMappingResponse {
    private Long id;
    private String simKey;
    private String icao;
}
