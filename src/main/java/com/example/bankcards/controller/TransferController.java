package com.example.bankcards.controller;

import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
@Tag(name = "API переводов", description = "Управление переводами между картами")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Сделать перевод с карты пользователя на другую карту этого пользователя. Доступ: USER")
    public CommonResponse<TransferResponse> createTransfer(@RequestBody @Valid CommonRequest<TransferRequest> request) {
        TransferResponse transferResponse = transferService.createTransfer(request.getBody());

        return CommonResponse.<TransferResponse>builder()
                .id(UUID.randomUUID())
                .body(transferResponse)
                .build();
    }
}
