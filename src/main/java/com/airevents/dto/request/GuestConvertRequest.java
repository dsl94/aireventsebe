package com.airevents.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestConvertRequest {
    private String email;
    private String membershipUntil;
}
