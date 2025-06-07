package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса со статусом активности карты")
public class CardStatusRequest {

    @NotNull(message = "Имя статуса активности карты не должно быть null")
    @Schema(description = "Статус активности карты", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "ACTIVE|LOCKED|EXPIRED", message = "Недопустимое имя статуса активности карты")
    private String status;
}
