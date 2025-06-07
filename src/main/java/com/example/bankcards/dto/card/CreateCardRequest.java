package com.example.bankcards.dto.card;

import com.example.bankcards.util.validation.ValidUUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса на создание карты")
public class CreateCardRequest {

    @NotNull(message = "Идентификатор владельца карты не должен быть null")
    @ValidUUID(message = "Идентификатор владельца карты не соотетствует формату UUID")
    @Schema(description = "Уникальный идентификатор владельца карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ownerId;

    @Valid
    @NotNull(message = "Срок действия карты не должен быть null")
    @Schema(description = "Срок действия карты", requiredMode = Schema.RequiredMode.REQUIRED)
    private ValidThruRequest validThru;

}
