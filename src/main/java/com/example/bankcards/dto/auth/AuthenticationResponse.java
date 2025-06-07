package com.example.bankcards.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO ответа после успешной аутентификации")
public class AuthenticationResponse {

    @Schema(description = "JWT", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @Schema(description = "Время жизни токена", requiredMode = Schema.RequiredMode.REQUIRED)
    private String expiresInMinutes;

    @Schema(description = "Тип токена", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tokenType;
}
