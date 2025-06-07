package com.example.bankcards.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO-обертка над телом ответа")
public class CommonResponse<T> {

    @Schema(description = "Уникальный идентификатор ответа", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "DTO ответа с полезной нагрузкой", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private T body;

    @Schema(description = "Сообщение об ошибке", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String errorMessage;

    @Schema(description = "Список DTO с ошибоками валидации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ValidationError> validationErrors;
}
