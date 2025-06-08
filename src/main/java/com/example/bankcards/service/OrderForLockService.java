package com.example.bankcards.service;

import com.example.bankcards.dto.orderforlock.CreateOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.FilterOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.OrderForLockResponse;
import com.example.bankcards.dto.orderforlock.UpdateOrderForLockRequest;

import java.util.List;
import java.util.UUID;

public interface OrderForLockService extends AuthorizedUserIdExtractor {

    OrderForLockResponse createOrderForLock(CreateOrderForLockRequest request);

    OrderForLockResponse updateOrderForLock(UUID id, UpdateOrderForLockRequest request);

    List<OrderForLockResponse> findByFilter(FilterOrderForLockRequest request);

    OrderForLockResponse getOrderForLock(UUID id);
}
