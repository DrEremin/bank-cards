package com.example.bankcards.controller;

import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.FilterUserRequest;
import com.example.bankcards.dto.user.UpdateUserRequest;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommonResponse<UserResponse> createUser(@RequestBody @Valid CommonRequest<CreateUserRequest> request) {
        UserResponse response = userService.createUser(request.getBody());

        return CommonResponse.<UserResponse>builder()
                .id(UUID.randomUUID())
                .body(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public CommonResponse<Void> deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);

        return CommonResponse.<Void>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @PatchMapping("/{id}")
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
    public CommonResponse<List<UserResponse>> findByFilter(
            @RequestBody @Valid CommonRequest<FilterUserRequest> request) {
        List<UserResponse> responses = userService.findByFilter(request.getBody());

        return CommonResponse.<List<UserResponse>>builder()
                .id(UUID.randomUUID())
                .body(responses)
                .build();
    }

    @GetMapping("/{id}")
    public CommonResponse<UserResponse> getUser(@PathVariable("id") UUID id) {
        UserResponse response = userService.getUser(id);

        return CommonResponse.<UserResponse>builder()
                .id(UUID.randomUUID())
                .body(response)
                .build();
    }
}
