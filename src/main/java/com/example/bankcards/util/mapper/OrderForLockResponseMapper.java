package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.orderforlock.OrderForLockResponse;
import com.example.bankcards.entity.OrderForLock;

public class OrderForLockResponseMapper {

    public static OrderForLockResponse fromOrderForLock(OrderForLock orderForLock) {

        return OrderForLockResponse.builder()
                .id(orderForLock.getId().toString())
                .status(orderForLock.getStatus().name())
                .build();
    }
}
