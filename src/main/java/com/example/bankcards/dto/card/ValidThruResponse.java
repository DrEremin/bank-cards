package com.example.bankcards.dto.card;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidThruResponse {

    private String expireYear;
    private String expireMonth;
}
