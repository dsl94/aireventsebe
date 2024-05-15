package com.airevents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaceTypeWithDistancesResponse {
    private Long id;
    private String name;
    private List<RaceDistanceResponse> distances;
}
