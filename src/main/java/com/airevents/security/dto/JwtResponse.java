package com.airevents.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private String location;
    private String expiration;
    private String username;
    private String airlineIcao;
    private String airlineName;
    private int numberOfBookings;
    private List<String> roles;

}
