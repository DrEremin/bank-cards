package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO ответа со сроком действия карты")
public class ValidThruResponse {

    @Schema(description = "Год окончания действия карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String expireYear;

    @Schema(description = "Месяц окончания действия карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String expireMonth;
}
