package com.airevents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChallengeResponse {
    private Long id;
    private String name;
    private double distance;
    private boolean male;
    private Map<String, Double> perMonth;
}
