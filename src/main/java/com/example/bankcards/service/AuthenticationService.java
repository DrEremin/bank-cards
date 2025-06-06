package com.example.bankcards.service;

import com.example.bankcards.dto.auth.AuthenticationRequest;
import com.example.bankcards.dto.auth.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticateUser(AuthenticationRequest request);
}
