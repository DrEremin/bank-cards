package com.example.bankcards.exception;

public class EntityNotFoundException extends BankCardsException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
