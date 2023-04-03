package com.airevents.dto.mapper;

import com.airevents.dto.request.RegisterRequest;
import com.airevents.dto.response.UserResponse;
import com.airevents.entity.Role;
import com.airevents.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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

    public static UserResponse entityToResponse(User entity) {
        return new UserResponse(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getVatsimId(),
                entity.getIvaoId(),
                entity.getPosconId(),
                entity.isActive(),
                entity.getMinutes(),
                entity.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                entity.getCreateDateTime().format(DATE_TIME_FORMATTER),
                entity.getUpdateDateTime().format(DATE_TIME_FORMATTER),
                entity.getFirstLoginDate() == null ? null : entity.getFirstLoginDate().format(DATE_TIME_FORMATTER),
                entity.getLastLoginDate() == null ? null : entity.getLastLoginDate().format(DATE_TIME_FORMATTER)
        );
    }

    public static List<UserResponse> entityToResponse(List<User> entities) {
        return entities.stream().map(UserMapper::entityToResponse).collect(Collectors.toList());
    }
}
