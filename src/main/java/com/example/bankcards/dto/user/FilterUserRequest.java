package com.example.bankcards.dto.user;

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
@Schema(description = "DTO запроса на получение списка пользователей с фильтрацией и сортировкой")
public class FilterUserRequest {

    @Schema(description = "Нижняя граница времени создания акканта пользователя",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createTimeFrom;

    @Schema(description = "Верхняя граница времени создания аккаунта пользователя",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime createTimeTo;

    @Valid
    @Schema(description = "Список DTO запроса с именем роли пользователя",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<RoleNameRequest> roles;

    @Valid
    @Builder.Default
    @Schema(description = "DTO запроса с данными по страницам и сортировками",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private PageableRequest page = new PageableRequest(1, 5, List.of());
}