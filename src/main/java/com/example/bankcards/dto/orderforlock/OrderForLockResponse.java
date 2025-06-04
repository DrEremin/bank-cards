package com.example.bankcards.dto.orderforlock;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderForLockResponse {

    private String cardId;
    private String cardNumber;
    private String cardLockStatus;
}
