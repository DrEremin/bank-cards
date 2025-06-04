package com.example.bankcards.dto.card;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardStatusRequest {

    @NotNull(message = "Имя статуса активности карты не должно быть null")
    @Pattern(regexp = "ACTIVE|LOCKED|EXPIRED", message = "Недопустимое имя статуса активности карты")
    private String status;
}
