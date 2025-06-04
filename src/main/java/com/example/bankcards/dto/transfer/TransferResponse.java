package com.example.bankcards.dto.transfer;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    private String id;
    private String sourceCardNumber;
    private String targetCardNUmber;
    private String amount;
    private LocalDateTime timestamp;
}
