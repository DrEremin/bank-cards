package com.example.bankcards.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса с данными по сортировкам")
public class SortRequest {

    @NotEmpty(message = "Не задано поле для сортировки")
    @Schema(description = "Имя поля по которому проводится сортировка", requiredMode = Schema.RequiredMode.REQUIRED)
    private String field;

    @Builder.Default
    @Pattern(regexp = "ASC|DESC", message = "Указано некорректное направление сортировки")
    @Schema(description = "Направление сортировки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String direction = "ASC";
}
