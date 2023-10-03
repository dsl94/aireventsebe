package com.airevents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaceReportResponse {
    private Long id;
    private String title;
    private String date;
    private String distance;
    private String info;
    private UserRaceResponse user;
}
