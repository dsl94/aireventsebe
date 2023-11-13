package com.airevents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StravaResponse {
    public String token_type;
    public int expires_at;
    public int expires_in;
    public String refresh_token;
    public String access_token;
    public StravaAthlete athlete;
}
