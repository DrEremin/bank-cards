package com.example.bankcards.service;

import com.example.bankcards.dto.card.*;

import java.util.List;
import java.util.UUID;

public interface CardService {

    CardResponse createCard(CreateCardRequest request);

    CardResponse updateCard(UUID cardId, UpdateCardRequest request);

    void deleteCard(UUID cardId);

    CardResponse findById(UUID cardId);

    List<CardResponse> findByFilter(FilterCardRequest request);

    BalanceResponse getBalanceById(UUID id);
}
