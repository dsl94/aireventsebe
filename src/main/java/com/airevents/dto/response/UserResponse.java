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
    private String gender;
    private String shirtSize;
    private String info;
    private String phone;
    private String email;
    private String stravaId;
    private List<String> roles;
    private String createdDate;
    private String updateDate;
    private String membershipUntil;
    private String firstLoginDate;
    private String lastLoginDate;
    private boolean stravaLogin;
}
