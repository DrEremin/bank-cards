package com.example.bankcards.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequest<T> {

    @Valid
    @NotNull(message = "Объект с данными в теле запроса не должен быть null")
    private T body;
}
