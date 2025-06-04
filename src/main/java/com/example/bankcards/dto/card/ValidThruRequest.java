package com.example.bankcards.dto.card;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidThruRequest {

    @NotNull(message = "Год срока действия карты не должен быть null")
    @Min(value = 2025, message = "Год срока действия карты не должен быть меньше 2025")
    private Integer expiryYear;

    @NotNull(message = "Месяц срока действия карты не должен быть null")
    @Min(value = 1, message = "Месяц срока действия карты не должен быть меньше 1")
    @Max(value = 12, message = "Месяц срока действия карты не должен больше 12")
    private Integer expiryMonth;
}
