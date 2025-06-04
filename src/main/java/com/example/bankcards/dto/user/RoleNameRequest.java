package com.example.bankcards.dto.user;

import com.example.bankcards.util.validation.ValidRoleName;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleNameRequest {

    @ValidRoleName
    @NotNull(message = "Имя роли не должно быть null")
    private String role;
}
