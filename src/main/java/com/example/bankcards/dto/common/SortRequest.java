package com.example.bankcards.dto.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortRequest {

    @NotEmpty(message = "Не задано поле для сортировки")
    private String field;

    @Builder.Default
    @Pattern(regexp = "ASC|DESC", message = "Указано некорректное направление сортировки")
    private String direction = "ASC";
}
