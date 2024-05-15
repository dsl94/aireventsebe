package com.airevents.controller;

import com.airevents.dto.mapper.RaceTypeMapper;
import com.airevents.dto.response.RaceTypeWithDistancesResponse;
import com.airevents.service.RaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/race-type")
public class RaceTypeController {

    @Autowired
    private RaceTypeService raceTypeService;

    @GetMapping
    ResponseEntity<List<RaceTypeWithDistancesResponse>> getAll() {
        return ResponseEntity.ok(raceTypeService.getAll());
    }
}
