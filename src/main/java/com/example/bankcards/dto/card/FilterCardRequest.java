package com.example.bankcards.dto.card;

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
public class FilterCardRequest {

    private String ownerId;
    private LocalDateTime expiredTimeForm;
    private LocalDateTime expiredTimeTo;
    private Boolean deleted;

    @Valid
    private List<CardStatusRequest> statuses;

    @Valid
    @Builder.Default
    private PageableRequest page = new PageableRequest(1, 5, List.of());
}
