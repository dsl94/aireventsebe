package com.airevents.service;

import com.airevents.dto.mapper.UserMapper;
import com.airevents.dto.request.RegisterRequest;
import com.airevents.entity.Role;
import com.airevents.entity.User;
import com.airevents.error.AirEventsException;
import com.airevents.error.ErrorCode;
import com.airevents.repository.RoleRepository;
import com.airevents.repository.UserRepository;
import com.airevents.security.JwtTokenUtil;
import com.airevents.security.RolesConstants;
import com.airevents.security.dto.JwtAuthenticationDto;
import com.airevents.security.dto.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        User user = UserMapper.registerRequestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Validate users email for duplicates
        Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(request.getEmail());
        if (userByEmail.isPresent()) {
            throw new AirEventsException(ErrorCode.USER_EXIST, "User with that email already exists");
        }

        Role role = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_USER.name());
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByUsernameIgnoreCase(email);
    }
}
