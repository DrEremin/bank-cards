package com.example.bankcards.dto.card;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardRequest {

    @Pattern(regexp = "ACTIVE|LOCKED|EXPIRED", message = "Недопустимое имя статуса активности карты")
    private String newStatus;

    @Valid
    private ValidThruRequest newValidThru;
}
