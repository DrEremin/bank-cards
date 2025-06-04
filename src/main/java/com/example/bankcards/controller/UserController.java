package com.example.bankcards.controller;

import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.FilterUserRequest;
import com.example.bankcards.dto.user.UpdateUserRequest;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommonResponse<UserResponse> createUser(
            @RequestBody @Valid CommonRequest<CreateUserRequest> request) {
        // Добавить логику

        return CommonResponse.<UserResponse>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> deleteUser(@PathVariable("id") UUID id) {
        // Добавить логику

        return CommonResponse.<Void>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @PatchMapping("/{id}")
    public CommonResponse<UserResponse> updateUser(@RequestBody @Valid CommonRequest<UpdateUserRequest> request) {
        // Добавить логику

        return CommonResponse.<UserResponse>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @PostMapping("/filter")
    public CommonResponse<List<UserResponse>> findByFilter(
            @RequestBody @Valid CommonRequest<FilterUserRequest> request) {
        // Добавить логику

        return CommonResponse.<List<UserResponse>>builder()
                .id(UUID.randomUUID())
                .build();
    }
}
