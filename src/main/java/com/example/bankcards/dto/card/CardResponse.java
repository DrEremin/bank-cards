package com.example.bankcards.dto.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardResponse {

    private String id;
    private String number;
    private String userId;
    private String status;
    private String balance;
    private ValidThruResponse validThru;
}
