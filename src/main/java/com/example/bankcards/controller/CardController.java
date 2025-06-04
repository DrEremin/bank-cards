package com.example.bankcards.controller;

import com.example.bankcards.dto.card.*;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommonResponse<CardResponse> createCard(@RequestBody @Valid CommonRequest<CreateCardRequest> request) {
        // Добавить логику

        return CommonResponse.<CardResponse>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public CommonResponse<Void> deleteCard(@PathVariable("id") UUID id) {
        // Добавить логику

        return CommonResponse.<Void>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @PatchMapping("/{id}")
    public CommonResponse<CardResponse> updateCard(
            @PathVariable("id") UUID cardId,
            @RequestBody @Valid CommonRequest<UpdateCardRequest> request) {
        // Добавить логику

        return CommonResponse.<CardResponse>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @PostMapping("/filter")
    public CommonResponse<List<CardResponse>> findByFilter(
            @RequestBody @Valid CommonRequest<FilterCardRequest> request) {
        //Добавить логику

        return CommonResponse.<List<CardResponse>>builder()
                .id(UUID.randomUUID())
                .build();
    }

    @GetMapping("/{id}")
    public CommonResponse<CardResponse> getBalance(@PathVariable("id") UUID id) {
        // Добавить логику

        return CommonResponse.<CardResponse>builder()
                .id(UUID.randomUUID())
                .build();
    }
}
