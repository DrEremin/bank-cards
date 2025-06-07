package com.example.bankcards.dto.card;

import com.example.bankcards.dto.common.PageableRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO запроса на получение списка карт с фильтрацией и сортировкой")
public class FilterCardRequest {

    @Schema(description = "Уникальный идентификатор владельца карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String ownerId;

    @Schema(description = "Нижняя граница времени создания карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createTimeFrom;

    @Schema(description = "Верхняя граница времени создания карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createTimeTo;

    @Schema(description = "Нижняя граница времени окончания срока действия карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime expiredTimeForm;

    @Schema(description = "Верхняя граница времени окончания срока действия карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime expiredTimeTo;

    @Valid
    @Schema(description = "Список DTO запроса со статусом активности карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<CardStatusRequest> statuses;

    @Valid
    @Builder.Default
    @Schema(description = "DTO запроса с данными по страницам и сортировкам",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private PageableRequest page = new PageableRequest(1, 5, List.of());
}
