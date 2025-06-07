package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса на обновление карты")
public class UpdateCardRequest {

    @Schema(description = "Новый статус карты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Pattern(regexp = "ACTIVE|LOCKED|EXPIRED", message = "Недопустимое имя статуса активности карты")
    private String newStatus;

    @Valid
    @Schema(description = "Срок действия карты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ValidThruRequest newValidThru;
}
