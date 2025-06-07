package com.example.bankcards.dto.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO ответа на перевод с карты на карту")
public class TransferResponse {

    @Schema(description = "Уникальный идентификатор транзакции",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "Номер карты-источника",
            example = "**** **** **** 3456",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String sourceCardNumber;

    @Schema(description = "Номер карты назначения",
            example = "**** **** **** 3456",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String targetCardNUmber;

    @Schema(description = "Сумма перевода",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String amount;

    @Schema(description = "Время исполнения транзакции",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime timestamp;
}
