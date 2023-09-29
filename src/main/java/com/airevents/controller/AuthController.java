package com.airevents.controller;

import com.airevents.dto.request.CreateUserRequest;
import com.airevents.entity.User;
import com.airevents.security.JwtTokenUtil;
import com.airevents.security.dto.JwtAuthenticationDto;
import com.airevents.security.dto.JwtResponse;
import com.airevents.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody CreateUserRequest request) {
        request.validate();
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<JwtResponse> createAuthenticationToken(
            @RequestBody JwtAuthenticationDto jwtAuthenticationDto)
            throws Exception {

        // Checking username|email and password
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtAuthenticationDto.getEmail(), jwtAuthenticationDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationDto.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);

        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.add(authority.getAuthority());
        }

        User userFromDb = authService.getByEmail(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(token, userFromDb.getId(), dateFormat.format(expiration), userFromDb.getEmail(), userFromDb.getFullName(), roles));
    }
}
