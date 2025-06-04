package com.example.bankcards.dto.orderforlock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderForLockRequest {

    @NotNull(message = "Действие по заявке на блокировку карты не должно быть null")
    @Pattern(regexp = "CANCEL_ORDER|COMPLETE_ORDER",
            message = "Недопустимое имя действия по заявке на блокировку карты")
    private String cardLockOrderAction;
}
