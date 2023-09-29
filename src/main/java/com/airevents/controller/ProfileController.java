package com.airevents.controller;

import com.airevents.dto.request.ChangePasswordRequest;
import com.airevents.dto.request.UpdateUserRequest;
import com.airevents.dto.response.UserResponse;
import com.airevents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<UserResponse> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();
        return ResponseEntity.ok(userService.getUserProfile(authenticatedUserName));
    }

    @PutMapping()
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UpdateUserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();
        return ResponseEntity.ok(userService.updateProfile(authenticatedUserName, request));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();
        userService.changePassword(authenticatedUserName, request);
        return ResponseEntity.ok().build();
    }
}
