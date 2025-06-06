package com.example.bankcards.dto.orderforlock;

import com.example.bankcards.dto.common.PageableRequest;
import com.example.bankcards.util.validation.ValidUUID;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterOrderForLockRequest {

    @ValidUUID(message = "Недопустимый формат индентификатора карты")
    private String cardId;
    private LocalDateTime createTimeFrom;
    private LocalDateTime createTimeTo;

    @Valid
    private List<OrderForLockStatusRequest> statuses;

    @Valid
    @Builder.Default
    private PageableRequest page = new PageableRequest(1, 5, List.of());
}
