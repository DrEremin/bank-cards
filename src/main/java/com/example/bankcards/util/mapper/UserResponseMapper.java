package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.User;

public class UserResponseMapper {

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .userName(user.getUserName())
                .build();
    }

    public static UserResponse fromUserWithRoles(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .userName(user.getUserName())
                .roles(user.getRoles().stream()
                        .map(r -> r.getRoleName().name())
                        .toList())
                .build();
    }
}
