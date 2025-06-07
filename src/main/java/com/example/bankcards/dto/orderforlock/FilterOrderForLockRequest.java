package com.example.bankcards.dto.orderforlock;

import com.example.bankcards.dto.common.PageableRequest;
import com.example.bankcards.util.validation.ValidUUID;
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
@Schema(description = "DTO запроса для получения списка заявок на блокировки карт с филтрацией и сортировкой")
public class FilterOrderForLockRequest {

    @Schema(description = "Уникальный идентификатор карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @ValidUUID(message = "Недопустимый формат индентификатора карты")
    private String cardId;

    @Schema(description = "Нижняя граница времени создания заявки на блокировку карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createTimeFrom;

    @Schema(description = "Верхняя граница времени создания заявки на блокировку карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createTimeTo;

    @Valid
    @Schema(description = "Список DTO запроса со статусом заявки на блокировку карты",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<OrderForLockStatusRequest> statuses;

    @Valid
    @Builder.Default
    @Schema(description = "DTO запроса с данными по страницам и сортировкам",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private PageableRequest page = new PageableRequest(1, 5, List.of());
}
