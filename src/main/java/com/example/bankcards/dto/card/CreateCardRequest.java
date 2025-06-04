package com.example.bankcards.dto.card;

import com.example.bankcards.util.validation.ValidUUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRequest {

    @NotNull(message = "Идентификатор владельца карты не должен быть null")
    @ValidUUID(message = "Идентификатор владельца карты не соотетствует формату UUID")
    private String ownerId;

    @Valid
    private ValidThruRequest validThru;

}
