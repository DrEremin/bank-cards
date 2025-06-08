package com.example.bankcards.controller;

import com.example.bankcards.dto.card.*;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
@Tag(name = "API карт", description = "Управление данными банковских карт")
public class CardController {

    private final CardService cardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Создать карту. Доступ: ADMIN")
    public CommonResponse<CardResponse> createCard(@RequestBody @Valid CommonRequest<CreateCardRequest> request) {
        CardResponse cardResponse = cardService.createCard(request.getBody());

        return CommonResponse.<CardResponse>builder()
                .id(UUID.randomUUID())
                .body(cardResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить карту. Доступ: ADMIN")
    public CommonResponse<Void> deleteCard(@PathVariable("id") UUID id) {
        cardService.deleteCard(id);

        return CommonResponse.<Void>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить статус/срок действия карты. Доступ: ADMIN")
    public CommonResponse<CardResponse> updateCard(
            @PathVariable("id") UUID cardId,
            @RequestBody @Valid CommonRequest<UpdateCardRequest> request) {
        CardResponse cardResponse = cardService.updateCard(cardId, request.getBody());

        return CommonResponse.<CardResponse>builder()
                .id(UUID.randomUUID())
                .body(cardResponse)
                .build();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Вывести список всех карт пользователя. Доступ: USER")
    public CommonResponse<List<CardResponse>> findAllByUser(
            @RequestParam(value = "size", defaultValue = "10") @Positive Integer size,
            @RequestParam(value = "page", defaultValue = "0") @Positive Integer page) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("createTime")));
        List<CardResponse> responses = cardService.findAllByUserId(pageable);

        return CommonResponse.<List<CardResponse>>builder()
                .id(UUID.randomUUID())
                .body(responses)
                .build();
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Вывести список карт c фильтрацией и сортировкой. Доступ: ADMIN")
    public CommonResponse<List<CardResponse>> findByFilter(
            @RequestBody @Valid CommonRequest<FilterCardRequest> request) {
        List<CardResponse> cardResponses = cardService.findByFilter(request.getBody());

        return CommonResponse.<List<CardResponse>>builder()
                .id(UUID.randomUUID())
                .body(cardResponses)
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Получить информацию о карте. Доступ: ADMIN, USER")
    public CommonResponse<CardResponse> getCard(@PathVariable("id") UUID id) {
        CardResponse cardResponse = cardService.findById(id);

        return CommonResponse.<CardResponse>builder()
                .id(UUID.randomUUID())
                .body(cardResponse)
                .build();
    }

    @GetMapping("/{id}/balance")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Получить баланс на карте. Доступ: USER")
    public CommonResponse<BalanceResponse> getBalance(@PathVariable("id") UUID cardId) {
        BalanceResponse balanceResponse = cardService.getBalanceById(cardId);

        return CommonResponse.<BalanceResponse>builder()
                .id(UUID.randomUUID())
                .body(balanceResponse)
                .build();
    }
}
