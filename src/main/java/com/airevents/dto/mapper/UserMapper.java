package com.airevents.dto.mapper;

import com.airevents.dto.request.CreateUserRequest;
import com.airevents.dto.response.UserResponse;
import com.airevents.entity.Role;
import com.airevents.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter REQUEST_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static User registerRequestToEntity(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail());
        user.setFullName(request.getFullName());
        user.setStravaId(request.getStravaId());
        user.setGender(request.getGender());
        user.setInfo(request.getInfo());
        user.setPhone(request.getPhone());
        if (request.getMembershipUntil() != null) {
            user.setMembershipUntil(LocalDate.parse(request.getMembershipUntil(), REQUEST_DATE_FORMAT).atStartOfDay());
        }
        if (request.getMedicalUntil() != null) {
            user.setMedicalUntil(LocalDate.parse(request.getMedicalUntil(), REQUEST_DATE_FORMAT).atStartOfDay());
        }
        return user;
    }

    public static UserResponse entityToResponse(User entity) {
        return new UserResponse(
                entity.getId(),
                entity.getFullName(),
                entity.getGender(),
                entity.getShirtSize(),
                entity.getInfo(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getStravaId(),
                entity.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                entity.getCreateDateTime().format(DATE_TIME_FORMATTER),
                entity.getUpdateDateTime().format(DATE_TIME_FORMATTER),
                entity.getMembershipUntil() == null ? null : entity.getMembershipUntil().format(DATE_FORMATTER),
                entity.getMedicalUntil() == null ? null : entity.getMedicalUntil().format(DATE_FORMATTER),
                entity.getFirstLoginDate() == null ? null : entity.getFirstLoginDate().format(DATE_TIME_FORMATTER),
                entity.getLastLoginDate() == null ? null : entity.getLastLoginDate().format(DATE_TIME_FORMATTER),
                entity.getStravaId() != null && entity.getUsername().contains("_") && !entity.getUsername().equals(entity.getEmail())
        );
    }

    public static List<UserResponse> entityToResponse(List<User> entities) {
        return entities.stream().map(UserMapper::entityToResponse).collect(Collectors.toList());
    }
}
