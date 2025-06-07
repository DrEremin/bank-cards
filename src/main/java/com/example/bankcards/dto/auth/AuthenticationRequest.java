package com.example.bankcards.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса с данными для аутентификации пользователя")
public class AuthenticationRequest {

    @NotBlank(message = "Имя пользователя не должно быть пустым или null")
    @Size(min = 4, max = 15, message = "Недопустимая длина имени пользователя")
    @Schema(description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;

    @NotBlank(message = "Пароль пользователя не должен быть пустым или null")
    @Size(min = 4, max = 15, message = "Недопустимая длина пароля пользователя")
    @Schema(description = "Пароль пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
