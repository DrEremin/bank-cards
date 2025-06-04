package com.example.bankcards.dto.common;

import jakarta.validation.Valid;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequest<T> {

    @Valid
    private T body;
}
