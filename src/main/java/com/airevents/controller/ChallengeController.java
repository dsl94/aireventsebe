package com.airevents.controller;

import com.airevents.dto.request.ChallengeRequest;
import com.airevents.dto.request.RaceRequest;
import com.airevents.dto.response.ChallengeResponse;
import com.airevents.dto.response.RaceResponse;
import com.airevents.service.ChallengeService;
import com.airevents.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @GetMapping()
    public ResponseEntity<List<ChallengeResponse>> getAll() {
        return ResponseEntity.ok(challengeService.getAll());
    }
    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody ChallengeRequest request) {
        challengeService.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChallengeResponse> update(@RequestBody ChallengeRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(challengeService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.getOne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        challengeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/go")
    public ResponseEntity<ChallengeResponse> goToRace(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();

        return ResponseEntity.ok(challengeService.goTo(authenticatedUserName, id));
    }

    @PutMapping("/{id}/no-go")
    public ResponseEntity<ChallengeResponse> dontGoToRace(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();
        challengeService.retractFrom(authenticatedUserName, id);
        return ResponseEntity.ok().build();
    }
}
