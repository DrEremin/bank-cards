package com.example.bankcards.dto.user;

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
public class UpdateUserRequest {

    @Size(min = 4, max = 15, message = "Недопустимая длина пароля пользователя")
    @Pattern(regexp = "^(?=\\s*\\S).*$", message = "Пароль пользователя не может быть пустым")
    private String newPassword;

    @Valid
    private List<RoleNameRequest> newRoles;
}
