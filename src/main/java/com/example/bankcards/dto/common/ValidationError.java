package com.example.bankcards.dto.common;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String field;
    private String message;
}
