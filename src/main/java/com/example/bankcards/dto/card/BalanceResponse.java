package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO ответа с балансом карты")
public class BalanceResponse {

    @Schema(description = "Баланс карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String balance;
}
