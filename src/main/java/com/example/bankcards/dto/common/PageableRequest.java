package com.example.bankcards.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO запроса с данными по страницам и сортировкам")
public class PageableRequest {

    @NotNull(message = "Не задан номер страницы")
    @Positive(message = "Номер страницы не может быть меньше 1")
    @Schema(description = "Номер страницы", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer number;

    @NotNull(message = "Не задан размер страницы")
    @Positive(message = "Размер страницы не может быть меньше 1")
    @Schema(description = "Размер страницы", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer size;

    @Valid
    @Builder.Default
    @Schema(description = "Список DTO запроса с данными по сортировкам", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<SortRequest> sorts = new ArrayList<>();
}
