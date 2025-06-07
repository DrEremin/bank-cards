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
@Schema(description = "DTO запроса для обновления статуса заявки на блокировку карты")
public class UpdateOrderForLockRequest {

    @NotNull(message = "Новый статус заявки на блокировку карты не должен быть null")
    @Pattern(regexp = "CANCELLED|COMPLETED",
            message = "Недопустимое имя нового статуса заявки на блокировку карты")
    @Schema(description = "Новый статус заявки на блокировку карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newStatus;
}
