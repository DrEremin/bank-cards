package com.example.bankcards.controller;

import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommonResponse<TransferResponse> createTransfer(@RequestBody @Valid CommonRequest<TransferRequest> request) {
        TransferResponse transferResponse = transferService.createTransfer(request.getBody());

        return CommonResponse.<TransferResponse>builder()
                .id(UUID.randomUUID())
                .body(transferResponse)
                .build();
    }
}
