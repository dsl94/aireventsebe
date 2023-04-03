package com.airevents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String vatsimId;
    private String ivaoId;
    private String posconId;
    private boolean isActive;
    private Long minutes;
    private List<String> roles;

    private String createdDate;
    private String updateDate;
    private String firstLoginDate;
    private String lastLoginDate;
}
