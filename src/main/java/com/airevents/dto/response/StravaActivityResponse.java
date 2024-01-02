package com.airevents.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StravaActivityResponse {
    private double distance;
    private String sport_type;
    private LocalDateTime start_date_local;
}
