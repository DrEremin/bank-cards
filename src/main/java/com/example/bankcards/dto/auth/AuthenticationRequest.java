package com.example.bankcards.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Имя пользователя не должно быть пустым или null")
    @Size(min = 4, max = 15, message = "Недопустимая длина имени пользователя")
    private String userName;

    @NotBlank(message = "Пароль пользователя не должен быть пустым или null")
    @Size(min = 4, max = 15, message = "Недопустимая длина пароля пользователя")
    private String password;
}
