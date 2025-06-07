package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO ответа с данными карты")
public class CardResponse {

    @Schema(description = "Уникальный идентификатор карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "Номер карты", example = "**** **** **** 3456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String number;

    @Schema(description = "Уникальный идентификатор владельца карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ownerId;

    @Schema(description = "Статус карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    @Schema(description = "Срок действия карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private ValidThruResponse validThru;
}
