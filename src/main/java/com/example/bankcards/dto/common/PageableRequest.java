package com.example.bankcards.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequest {

    @NotNull(message = "Не задан номер страницы")
    @Positive(message = "Номер страницы не может быть меньше 1")
    private Integer number;

    @NotNull(message = "Не задан размер страницы")
    @Positive(message = "Размер страницы не может быть меньше 1")
    private Integer size;

    @Valid
    @Builder.Default
    private List<SortRequest> sorts = new ArrayList<>();
}
