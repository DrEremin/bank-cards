package com.example.bankcards.controller;

import com.example.bankcards.dto.orderforlock.OrderForLockResponse;
import com.example.bankcards.dto.orderforlock.CreateOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.FilterOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.UpdateOrderForLockRequest;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders-for-locks")
public class OrderForLockController {

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommonResponse<OrderForLockResponse> createOrderForLock(
            @RequestBody @Valid CommonRequest<CreateOrderForLockRequest> request) {
        // Добавить логику

        return CommonResponse.<OrderForLockResponse>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> deleteOrderForLock(@PathVariable("id") UUID id) {
        // Добавить логику

        return CommonResponse.<Void>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @PatchMapping("/{id}")
    public CommonResponse<OrderForLockResponse> updateOrderForLock(
            @PathVariable("id") UUID cardLockOrderId,
            @RequestBody @Valid CommonRequest<UpdateOrderForLockRequest> request) {
        // Добавить логику

        return CommonResponse.<OrderForLockResponse>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @PostMapping("/filter")
    public CommonResponse<List<OrderForLockResponse>> findByFilter(
            @RequestBody @Valid CommonRequest<FilterOrderForLockRequest> request) {
        //Добавить логику

        return CommonResponse.<List<OrderForLockResponse>>builder()
                .id(UUID.randomUUID())
                .build();
    }
}
