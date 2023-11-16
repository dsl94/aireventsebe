package com.airevents.dto.response;

import lombok.Data;

@Data
public class StravaLoginResponse {
    private String token_type;
    private String access_token;
    private Long expires_at;
    private Long expires_in;
    private String refresh_token;
}
