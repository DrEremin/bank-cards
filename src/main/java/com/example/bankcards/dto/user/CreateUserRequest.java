package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса на создание аккаунта пользователя")
public class CreateUserRequest {

    @NotBlank(message = "Имя пользователя не должно быть пустым или null")
    @Size(min = 4, max = 15, message = "Недопустимая длина имени пользователя")
    @Schema(description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;

    @NotBlank(message = "Пароль пользователя не должен быть пустым или null")
    @Size(min = 4, max = 15, message = "Недопустимая длина пароля пользователя")
    @Schema(description = "Пароль пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Valid
    @NotEmpty(message = "Список ролей пользователя не должен быть пустым или null")
    @Schema(description = "Список DTO запроса с именем роли", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<RoleNameRequest> roles;
}
