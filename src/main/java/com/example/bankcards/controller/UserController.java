package com.example.bankcards.controller;

import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.FilterUserRequest;
import com.example.bankcards.dto.user.UpdateUserRequest;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "API аккаунтов пользователей", description = "Управление данными аккаунтов пользователей")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Создать аккаунт пользователя. Доступ: ADMIN")
    public CommonResponse<UserResponse> createUser(@RequestBody @Valid CommonRequest<CreateUserRequest> request) {
        UserResponse response = userService.createUser(request.getBody());

        return CommonResponse.<UserResponse>builder()
                .id(UUID.randomUUID())
                .body(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить аккаунт пользователя. Доступ: ADMIN")
    public CommonResponse<Void> deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);

        return CommonResponse.<Void>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить данные пользователя (пароль/роли). Доступ: ADMIN")
    public CommonResponse<UserResponse> updateUser(
            @PathVariable("id") UUID id,
            @RequestBody @Valid CommonRequest<UpdateUserRequest> request) {
        UserResponse response = userService.updateUser(id, request.getBody());

        return CommonResponse.<UserResponse>builder()
                .id(UUID.randomUUID())
                .body(response)
                .build();
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список пользователей с фильтрацией и сортировкой. Доступ: ADMIN")
    public CommonResponse<List<UserResponse>> findByFilter(
            @RequestBody @Valid CommonRequest<FilterUserRequest> request) {
        List<UserResponse> responses = userService.findByFilter(request.getBody());

        return CommonResponse.<List<UserResponse>>builder()
                .id(UUID.randomUUID())
                .body(responses)
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить данные пользователя. Доступ: ADMIN")
    public CommonResponse<UserResponse> getUser(@PathVariable("id") UUID id) {
        UserResponse response = userService.getUser(id);

        return CommonResponse.<UserResponse>builder()
                .id(UUID.randomUUID())
                .body(response)
                .build();
    }
}
