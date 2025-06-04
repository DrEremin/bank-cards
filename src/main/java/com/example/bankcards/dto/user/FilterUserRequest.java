package com.example.bankcards.dto.user;

import com.example.bankcards.dto.common.PageableRequest;
import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterUserRequest {

    private String userName;
    private Boolean deleted;

    @Valid
    private List<RoleNameRequest> roles;

    @Valid
    @Builder.Default
    private PageableRequest page = new PageableRequest(1, 5, List.of());
}
