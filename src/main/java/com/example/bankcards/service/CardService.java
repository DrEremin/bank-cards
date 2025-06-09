package com.example.bankcards.service;

import com.example.bankcards.dto.card.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CardService extends AuthorizedUserIdExtractor {

    CardResponse createCard(CreateCardRequest request);

    CardResponse updateCard(UUID cardId, UpdateCardRequest request);

    void deleteCard(UUID cardId);

    CardResponse findById(UUID cardId);

    List<CardResponse> findAllByUserId(Pageable pageable);

    List<CardResponse> findByFilter(FilterCardRequest request);

    BalanceResponse getBalanceById(UUID id);

    Integer updateExpiredCardsStatus();
}
