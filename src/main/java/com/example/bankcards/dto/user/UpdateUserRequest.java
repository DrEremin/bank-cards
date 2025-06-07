package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса на обновление данных пользователя")
public class UpdateUserRequest {

    @Schema(description = "Новвый пароль пользователя",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(min = 4, max = 15, message = "Недопустимая длина пароля пользователя")
    @Pattern(regexp = "^(?=\\s*\\S).*$", message = "Пароль пользователя не может быть пустым")
    private String newPassword;

    @Valid
    @Schema(description = "Список DTO запроса с именем роли пользователя",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<RoleNameRequest> newRoles;
}
