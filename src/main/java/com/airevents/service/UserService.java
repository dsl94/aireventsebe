package com.airevents.service;

import com.airevents.dto.mapper.UserMapper;
import com.airevents.dto.response.UserResponse;
import com.airevents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return UserMapper
                .entityToResponse(
                        userRepository.findAll()
                );
    }
}
