package com.example.bankcards.controller;

import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.dto.common.ValidationError;
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
        log.error("Непредвиденное исключение: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Непредвиденное исключение: " + e.getMessage())
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
}
