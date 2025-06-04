package com.example.bankcards.dto.orderforlock;

import com.example.bankcards.dto.common.PageableRequest;
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

    private String cardId;
    private LocalDateTime createTimeFrom;
    private LocalDateTime createTimeTo;
    private Boolean deleted;

    @Valid
    private List<OrderForLockStatusRequest> statuses;

    @Valid
    @Builder.Default
    private PageableRequest page = new PageableRequest(1, 5, List.of());
}
