package com.example.bankcards.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO ответа c информацией об ошибке валидации")
public class ValidationError {

    @Schema(description = "Имя невалидного поля", requiredMode = Schema.RequiredMode.REQUIRED)
    private String field;

    @Schema(description = "Сообщение о причине ошибки", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}
