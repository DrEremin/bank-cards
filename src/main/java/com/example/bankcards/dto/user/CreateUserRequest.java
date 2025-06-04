package com.example.bankcards.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Имя пользователя не должно быть пустым или null")
    @Size(min = 4, max = 15, message = "Недопустимая длина имени пользователя")
    private String userName;

    @NotBlank(message = "Пароль пользователя не должен быть пустым или null")
    @Size(min = 4, max = 15, message = "Недопустимая длина пароля пользователя")
    private String password;

    @Valid
    @NotEmpty(message = "Список ролей пользователя не должен быть пустым или null")
    private List<RoleNameRequest> roles;
}
