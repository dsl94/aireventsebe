package com.airevents.service;

import com.airevents.dto.mapper.UserMapper;
import com.airevents.dto.request.CreateUserRequest;
import com.airevents.entity.Role;
import com.airevents.entity.User;
import com.airevents.error.RcnException;
import com.airevents.error.ErrorCode;
import com.airevents.repository.RoleRepository;
import com.airevents.repository.UserRepository;
import com.airevents.security.RolesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(CreateUserRequest request) {
        User user = UserMapper.registerRequestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Validate users email for duplicates
        Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(request.getEmail());
        if (userByEmail.isPresent()) {
            throw new RcnException(ErrorCode.USER_EXIST, "User with that email already exists");
        }

        Role role = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_USER.name());
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
    }

    public User getByEmail(String email) {
        User user = userRepository.findByUsernameIgnoreCase(email);
        if (user.getFirstLoginDate() == null) {
            user.setFirstLoginDate(LocalDateTime.now());
            user.setLastLoginDate(LocalDateTime.now());
        } else {
            user.setLastLoginDate(LocalDateTime.now());
        }

        userRepository.save(user);

        return user;
    }
}
