package com.airevents.controller;

import com.airevents.dto.request.CreateRaceReportRequest;
import com.airevents.dto.request.RaceRequest;
import com.airevents.dto.response.RaceReportResponse;
import com.airevents.dto.response.RaceResponse;
import com.airevents.service.RaceReportService;
import com.airevents.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/race-report")
public class RaceReportController {
    @Autowired
    private RaceReportService raceReportService;

    @GetMapping()
    public ResponseEntity<List<RaceReportResponse>> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();
        return ResponseEntity.ok(raceReportService.all(authenticatedUserName));
    }

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody CreateRaceReportRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();

        raceReportService.createRaceReport(request, authenticatedUserName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        raceReportService.deleteRaceReport(id);
        return ResponseEntity.ok().build();
    }
}
