package com.airevents.service;

import com.airevents.dto.mapper.UserMapper;
import com.airevents.dto.request.CreateUserRequest;
import com.airevents.dto.request.UpdateUserRequest;
import com.airevents.dto.response.UserResponse;
import com.airevents.entity.Role;
import com.airevents.entity.User;
import com.airevents.error.ErrorCode;
import com.airevents.error.RcnException;
import com.airevents.repository.RoleRepository;
import com.airevents.repository.UserRepository;
import com.airevents.security.RolesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final DateTimeFormatter REQUEST_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public List<UserResponse> getAllUsers() {
        return UserMapper
                .entityToResponse(
                        userRepository.findAll()
                );
    }

    public void createUser(CreateUserRequest request) {
        User user = UserMapper.registerRequestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Validate users email for duplicates
        Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(request.getEmail());
        if (userByEmail.isPresent()) {
            throw new RcnException(ErrorCode.USER_EXIST, "Korisnik sa tim emailom vec postoji");
        }

        Role role = roleRepository.findByRoleIgnoreCase(RolesConstants.ROLE_USER.name());
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
    }

    public UserResponse update(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Korisnik ne postoji"));
        user.setFullName(request.getFullName());
        user.setMembershipUntil(LocalDate.parse(request.getMembershipUntil(), REQUEST_DATE_FORMAT).atStartOfDay());

        return UserMapper.entityToResponse(userRepository.save(user));
    }

    public UserResponse getById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Korisnik ne postoji"));
        return UserMapper.entityToResponse(user);
    }

    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RcnException(ErrorCode.NOT_FOUND, "Korisnik ne postoji"));
        userRepository.delete(user);
    }
}
