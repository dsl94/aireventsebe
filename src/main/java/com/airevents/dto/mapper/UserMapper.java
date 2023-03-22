package com.airevents.dto.mapper;

import com.airevents.dto.request.RegisterRequest;
import com.airevents.entity.User;

public class UserMapper {
    public static User registerRequestToEntity(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail());
        user.setFullName(request.getFullName());
        user.setVatsimId(request.getVatsimId());
        user.setIvaoId(request.getIvaoId());
        user.setPosconId(request.getPosconId());

        return user;
    }
}
