package com.example.bankcards.exception;

public class UniquenessViolationException extends BankCardsException {

    public UniquenessViolationException(String message) {
        super(message);
    }

    public UniquenessViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
