package com.example.bankcards.dto.orderforlock;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса со статусом заявки на блокировку карты")
public class OrderForLockStatusRequest {

    @NotNull(message = "Имя статуса заявки на блокировку карты не должно быть null")
    @Pattern(regexp = "PENDING|CANCELLED|COMPLETED",
            message = "Недопустимое имя статуса заявки на блокировку карты")
    @Schema(description = "Статус заявки на блокировку карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;
}
