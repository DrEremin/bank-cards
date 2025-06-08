package com.example.bankcards.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.bankcards.util.property.SecurityProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTProvider {

    private final SecurityProperty securityProperty;

    public String generateToken(String userName) {
        Date expirationDate = Date.from(ZonedDateTime.now()
                .plusMinutes(securityProperty.getJwtExpiresInMinutes()).toInstant()
        );

        return JWT.create()
                .withSubject("User info")
                .withClaim("userName", userName)
                .withIssuedAt(new Date())
                .withIssuer("back-cards-app")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(securityProperty.getJwtSecret()));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(securityProperty.getJwtSecret()))
                .withSubject("User info")
                .withIssuer("back-cards-app")
                .build();
        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaim("userName").asString();
    }
}
