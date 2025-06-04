package com.example.bankcards.dto.orderforlock;

import com.example.bankcards.util.validation.ValidUUID;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderForLockRequest {

    @NotNull(message = "Идентификатор карты не должен быть null")
    @ValidUUID(message = "Идентификатор карты не соотетствует формату UUID")
    private String cardId;
}
