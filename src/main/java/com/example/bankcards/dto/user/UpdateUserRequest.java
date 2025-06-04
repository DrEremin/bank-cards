package com.example.bankcards.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "Пароль пользователя не должен быть пустым или null")
    @Size(min = 4, max = 15, message = "Недопустимая длина пароля пользователя")
    private String newPassword;

    @Valid
    @NotEmpty(message = "Список ролей пользователя не должен быть пустым или null")
    private List<RoleNameRequest> newRoles;
}
