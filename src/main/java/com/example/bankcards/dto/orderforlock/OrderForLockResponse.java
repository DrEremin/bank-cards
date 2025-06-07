package com.example.bankcards.dto.orderforlock;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO ответа при получении заявки на блокировку карты")
public class OrderForLockResponse {

    @Schema(description = "Уникальный идентификатор заявки на блокировку карты",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "Статус заявки на блокировку карты",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;
}
