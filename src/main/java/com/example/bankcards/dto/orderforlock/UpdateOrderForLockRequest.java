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

    @NotNull(message = "Новый статус заявки на блокировку карты не должен быть null")
    @Pattern(regexp = "CANCELLED|COMPLETED",
            message = "Недопустимое имя нового статуса заявки на блокировку карты")
    private String newStatus;
}
