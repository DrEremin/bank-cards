package com.example.bankcards.util.property;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "bank-cards.security")
public class SecurityProperty {

    @NotNull(message = "JWT secret не должно быть null")
    private String jwtSecret;

    @NotNull(message = "Время жизни JWT не должно быть null")
    private Integer jwtExpiresInMinutes;

    @NotNull(message = "Пароль для TextEncryptor не должен быть null")
    private String textEncryptorPassword;

    @NotNull(message = "Пароль для первого пользователя не должен быть null")
    private String firstAdminPassword;

    private List<String> corsUrls = List.of();

    private List<String> corsMethods = List.of();

    private List<String> corsHeaders = List.of();
}
