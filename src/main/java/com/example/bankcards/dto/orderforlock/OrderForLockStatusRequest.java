package com.example.bankcards.dto.orderforlock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderForLockStatusRequest {

    @NotNull(message = "Имя статуса заявки на блокировку карты не должно быть null")
    @Pattern(regexp = "PENDING|CANCELLED|COMPLETED",
            message = "Недопустимое имя статуса заявки на блокировку карты")
    private String status;
}
