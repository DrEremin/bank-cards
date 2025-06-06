package com.example.bankcards.util.property;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "bank-cards.jwt")
public class JWTProperty {

    @NotNull(message = "Secret не должно быть null")
    private String secret;

    @NotNull(message = "Время жизни JWT не должно быть null")
    private Integer expiresInMinutes;
}
