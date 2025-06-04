package com.example.bankcards.dto.card;

import jakarta.validation.Valid;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardRequest {

    @Valid
    private CardStatusRequest status;

    @Valid
    private ValidThruRequest validThru;
}
