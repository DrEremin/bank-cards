package com.example.bankcards.service;

import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.FilterUserRequest;
import com.example.bankcards.dto.user.UpdateUserRequest;
import com.example.bankcards.dto.user.UserResponse;

import java.util.List;
import java.util.UUID;


public interface UserService extends AuthorizedUserIdExtractor {

    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    void deleteUser(UUID userId);

    List<UserResponse> findByFilter(FilterUserRequest request);

    UserResponse getUser(UUID id);
}
