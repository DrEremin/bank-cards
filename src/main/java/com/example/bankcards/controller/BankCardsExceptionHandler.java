package com.example.bankcards.controller;

import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.dto.common.ValidationError;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.UniquenessViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class BankCardsExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<?> handleException(Exception e) {
        log.error("Непредвиденная ошибка: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Непредвиденная ошибка: " + e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ValidationError> validationErrors = bindingResult.getFieldErrors().stream()
                .map(fieldEx -> ValidationError.builder()
                        .field(fieldEx.getField())
                        .message(fieldEx.getDefaultMessage())
                        .build())
                .toList();
        log.warn("Ошибка валидации: {}", validationErrors);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Ошибка валидации")
                .validationErrors(validationErrors)
                .build();
    }

    @ExceptionHandler(UniquenessViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResponse<?> handleUniquenessViolationException(UniquenessViolationException e) {
        log.warn("Ошибка нарушения ограничения уникальности ресурса: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Ошибка нарушения ограничения уникальности ресурса: " + e.getMessage())
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<?> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("Ошибка, ресурс не найден: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Ошибка, ресурс не найден: " + e.getMessage())
                .build();
    }

    @ExceptionHandler(BankCardsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleBankCardsException(BankCardsException e) {
        log.warn("Ошибка валидации: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Ошибка валидации: " + e.getMessage())
                .build();
    }
}
