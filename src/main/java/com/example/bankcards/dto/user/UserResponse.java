package com.example.bankcards.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO ответа с данными пользователя")
public class UserResponse {

    @Schema(description = "Уникальный идентификатор пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;

    @Schema(description = "Список ролей пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> roles;
}
