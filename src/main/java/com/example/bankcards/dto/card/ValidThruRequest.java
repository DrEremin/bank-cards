package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса со сроком действия карты")
public class ValidThruRequest {

    @NotNull(message = "Год срока действия карты не должен быть null")
    @Min(value = 2025, message = "Год срока действия карты не должен быть меньше 2025")
    @Schema(description = "Год окончания действия карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer expiryYear;

    @NotNull(message = "Месяц срока действия карты не должен быть null")
    @Max(value = 12, message = "Месяц срока действия карты не должен больше 12")
    @Min(value = 1, message = "Месяц срока действия карты не должен быть меньше 1")
    @Schema(description = "Месяц окончания действия карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer expiryMonth;
}