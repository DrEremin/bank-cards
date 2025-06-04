package com.example.bankcards.controller;

import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommonResponse<TransferResponse> createTransfer(@RequestBody @Valid CommonRequest<TransferRequest> request) {
        // логика

        return CommonResponse.<TransferResponse>builder()
                .id(UUID.randomUUID())
                .build();
    }
}
