package com.example.bankcards.service.impl;

import com.example.bankcards.dto.auth.AuthenticationRequest;
import com.example.bankcards.dto.auth.AuthenticationResponse;
import com.example.bankcards.security.JWTProvider;
import com.example.bankcards.service.AuthenticationService;
import com.example.bankcards.util.property.SecurityProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationProvider authenticationProvider;
    private final JWTProvider jwtProvider;
    private final SecurityProperty securityProperty;

    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );

        String token = jwtProvider.generateToken(request.getUserName());
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token(token)
                .expiresInMinutes(securityProperty.getJwtExpiresInMinutes().toString())
                .tokenType("Bearer")
                .build();
        log.info("Аутентификация пользователя {} пройдена успешно", request.getUserName());

        return response;
    }
}
