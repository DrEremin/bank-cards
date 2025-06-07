package com.example.bankcards.controller;

import com.example.bankcards.dto.orderforlock.OrderForLockResponse;
import com.example.bankcards.dto.orderforlock.CreateOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.FilterOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.UpdateOrderForLockRequest;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.service.OrderForLockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lock-orders")
@RequiredArgsConstructor
@Tag(name = "API запросов на блокировки карт", description = "Управление запросами на блокироки карт")
public class OrderForLockController {

    private final OrderForLockService orderForLockService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Создать запрос на блокировку карты. Доступ: USER")
    public CommonResponse<OrderForLockResponse> createOrderForLock(
            @RequestBody @Valid CommonRequest<CreateOrderForLockRequest> request) {
        OrderForLockResponse response = orderForLockService.createOrderForLok(request.getBody());

        return CommonResponse.<OrderForLockResponse>builder()
                .id(UUID.randomUUID())
                .body(response)
                .build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить статус запроса на блокировку карты. Доступ: ADMIN")
    public CommonResponse<OrderForLockResponse> updateOrderForLock(
            @PathVariable("id") UUID id,
            @RequestBody @Valid CommonRequest<UpdateOrderForLockRequest> request) {
        OrderForLockResponse response = orderForLockService.updateOrderForLock(id, request.getBody());

        return CommonResponse.<OrderForLockResponse>builder()
                .id(UUID.randomUUID())
                .body(response)
                .build();
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список запросов на блокировки карт с фильтрацией и сортировкой. Доступ: ADMIN")
    public CommonResponse<List<OrderForLockResponse>> findByFilter(
            @RequestBody @Valid CommonRequest<FilterOrderForLockRequest> request) {
        List<OrderForLockResponse> responses = orderForLockService.findByFilter(request.getBody());

        return CommonResponse.<List<OrderForLockResponse>>builder()
                .id(UUID.randomUUID())
                .body(responses)
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить данные запроса на блокировку карты. Доступ: ADMIN")
    public CommonResponse<OrderForLockResponse> getOrderForLock(@PathVariable("id") UUID id) {
        OrderForLockResponse response = orderForLockService.getOrderForLock(id);

        return CommonResponse.<OrderForLockResponse>builder()
                .id(UUID.randomUUID())
                .body(response)
                .build();
    }
}
