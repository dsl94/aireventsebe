package com.airevents.controller;

import com.airevents.dto.request.CreateUserRequest;
import com.airevents.dto.request.RaceRequest;
import com.airevents.dto.request.UpdateUserRequest;
import com.airevents.dto.response.RaceResponse;
import com.airevents.dto.response.UserResponse;
import com.airevents.entity.Race;
import com.airevents.service.RaceService;
import com.airevents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/race")
public class RaceController {

    @Autowired
    private RaceService raceService;

    @GetMapping()
    public ResponseEntity<List<RaceResponse>> getAll() {
        return ResponseEntity.ok(raceService.getAllRaces());
    }
    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody RaceRequest request) {
        raceService.createRace(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RaceResponse> update(@RequestBody RaceRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(raceService.updateRace(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(raceService.getRace(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        raceService.deleteRace(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/go")
    public ResponseEntity<RaceResponse> goToRace(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();

        return ResponseEntity.ok(raceService.goToRace(authenticatedUserName, id));
    }

    @PutMapping("/{id}/no-go")
    public ResponseEntity<RaceResponse> dontGoToRace(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();

        return ResponseEntity.ok(raceService.retractFromRace(authenticatedUserName, id));
    }
}
