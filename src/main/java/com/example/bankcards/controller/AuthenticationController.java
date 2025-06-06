package com.example.bankcards.controller;

import com.example.bankcards.dto.auth.AuthenticationRequest;
import com.example.bankcards.dto.auth.AuthenticationResponse;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public CommonResponse<AuthenticationResponse> signUp(@RequestBody CommonRequest<AuthenticationRequest> request) {
        AuthenticationResponse response = authenticationService.authenticateUser(request.getBody());

        return CommonResponse.<AuthenticationResponse>builder()
                .id(UUID.randomUUID())
                .body(response)
                .build();
    }
}
