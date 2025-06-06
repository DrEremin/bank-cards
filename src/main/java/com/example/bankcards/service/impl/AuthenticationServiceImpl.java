package com.example.bankcards.service.impl;

import com.example.bankcards.dto.auth.AuthenticationRequest;
import com.example.bankcards.dto.auth.AuthenticationResponse;
import com.example.bankcards.security.JWTProvider;
import com.example.bankcards.service.AuthenticationService;
import com.example.bankcards.util.property.JWTProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;
    private final JWTProperty jwtProperty;

    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );

        String token = jwtProvider.generateToken(request.getUserName());

        return AuthenticationResponse.builder()
                .token(token)
                .expiresInMinutes(jwtProperty.getExpiresInMinutes().toString())
                .tokenType("Bearer")
                .build();
    }
}
