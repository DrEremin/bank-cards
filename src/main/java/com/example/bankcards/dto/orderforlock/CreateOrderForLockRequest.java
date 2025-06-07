package com.example.bankcards.dto.orderforlock;

import com.example.bankcards.util.validation.ValidUUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса на создание заявки для блокировки карты")
public class CreateOrderForLockRequest {

    @NotNull(message = "Идентификатор карты не должен быть null")
    @ValidUUID(message = "Идентификатор карты не соотетствует формату UUID")
    @Schema(description = "Уникальный идентификатор карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cardId;
}
