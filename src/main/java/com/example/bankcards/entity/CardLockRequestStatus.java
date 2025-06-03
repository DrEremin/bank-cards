package com.example.bankcards.entity;

/**
 * Варианты состояний запроса на блокировку карты
 */
public enum CardLockRequestStatus {

    PENDING,
    CANCELLED,
    COMPLETED
}
