package com.example.bankcards.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO-обертка над телом запроса")
public class CommonRequest<T> {

    @Valid
    @NotNull(message = "Объект с данными в теле запроса не должен быть null")
    @Schema(description = "DTO запроса с полезной нагрузкой", requiredMode = Schema.RequiredMode.REQUIRED)
    private T body;
}
