package com.airevents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StravaAthlete {
    public int id;
    public String firstname;
    public String lastname;
    public String city;
    public String state;
    public String country;
    public String sex;
}
