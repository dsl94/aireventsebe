package com.airevents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceResponse {
    private Long id;
    private String title;
    private String date;
    private String distances;
    private List<String> users;
}
