package com.example.bankcards.dto.user;

import com.example.bankcards.util.validation.ValidRoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса с именем роли пользователя")
public class RoleNameRequest {

    @ValidRoleName
    @NotNull(message = "Имя роли не должно быть null")
    @Schema(description = "Имя роли пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;
}
