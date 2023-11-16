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
    private Long id;
    private String expiration;
    private String email;
    private String fullName;
    private List<String> roles;
    private boolean stravaLogin;
    private boolean hasStrava;
}
