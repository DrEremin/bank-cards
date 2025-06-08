package com.example.bankcards.exception;

import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.dto.common.ValidationError;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<?> handleInvalidFormatException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException ife) {
            String path = ife.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));
            String errMessage = "Ошибка валидации, указан некорректный формат поля: " + path;
            log.error(errMessage, e);

            return CommonResponse.builder()
                    .id(UUID.randomUUID())
                    .errorMessage(errMessage)
                    .build();
        }

        return handleException(e);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<?> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("Ошибка подтверждения данных: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Ошибка подтверждения данных: " + e.getMessage())
                .build();
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResponse<?> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.warn("Ошибка аутентификации/авторизации: {}", e.getMessage());

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Ошибка аутентификации/авторизации: " + e.getMessage())
                .build();
    }
}
