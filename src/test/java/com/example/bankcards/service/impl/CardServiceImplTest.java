package com.example.bankcards.service.impl;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.card.CreateCardRequest;
import com.example.bankcards.dto.card.UpdateCardRequest;
import com.example.bankcards.dto.card.ValidThruRequest;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CardServiceImplTest extends AbstractTest {

    private final UUID cardId = UUID.randomUUID();
    private final String cardNumber = "1234567891234567";

    private final User user = User.builder()
            .id(UUID.randomUUID())
            .userName("Test")
            .roles(Set.of(new Role(UUID.randomUUID(), RoleName.ROLE_USER)))
            .build();

    private final CreateCardRequest createCardRequest = CreateCardRequest.builder()
            .ownerId(user.getId().toString())
            .build();

    private final UpdateCardRequest updateCardRequest = UpdateCardRequest.builder()
            .newStatus(CardStatus.ACTIVE.name())
            .build();

    private final Card card = Card.builder()
            .id(cardId)
            .encodedNumber("1234567891234567")
            .owner(user)
            .status(CardStatus.ACTIVE)
            .balance(new BigDecimal("0.00"))
            .expiredTime(LocalDateTime.now().plusYears(2))
            .build();

    @Test
    void createCard_ValidThruIsLessThanAllowedValue_expect_bankCardsException() {
        LocalDateTime currentTime = LocalDateTime.now();
        when(mockValidPeriodProperty.getMinMonths())
                .thenReturn(1);

        createCardRequest.setValidThru(new ValidThruRequest(currentTime.getYear(), currentTime.getMonthValue()));

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> cardService.createCard(createCardRequest));
        assertEquals("Срок действия карты не может быть меньше %d месяцев".formatted(mockValidPeriodProperty.getMinMonths()),
                ex.getMessage());

        verify(mockValidPeriodProperty, times(3))
                .getMinMonths();
        verify(mockValidPeriodProperty, never())
                .getMaxMonths();
        verify(mockCardRepository, never())
                .save(any(Card.class));
        verify(mockUserRepository, never())
                .findById(any(UUID.class));
    }

    @Test
    void createCard_ValidThruIsGreaterThanAllowedValue_expect_bankCardsException() {
        LocalDateTime currentTime = LocalDateTime.now().plusMonths(4);
        when(mockValidPeriodProperty.getMinMonths())
                .thenReturn(2);
        when(mockValidPeriodProperty.getMaxMonths())
                .thenReturn(3);

        createCardRequest.setValidThru(new ValidThruRequest(currentTime.getYear(), currentTime.getMonthValue()));

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> cardService.createCard(createCardRequest));
        assertEquals("Срок действия карты не может быть больше %d месяцев".formatted(mockValidPeriodProperty.getMaxMonths()),
                ex.getMessage());

        verify(mockValidPeriodProperty, times(1))
                .getMinMonths();
        verify(mockValidPeriodProperty, times(3))
                .getMaxMonths();
        verify(mockCardRepository, never())
                .save(any(Card.class));
        verify(mockUserRepository, never())
                .findById(any(UUID.class));
    }

    @Test
    void createCard_userDoesNotExist_expect_entityNotFoundException() {
        LocalDateTime currentTime = LocalDateTime.now().plusMonths(4);
        when(mockValidPeriodProperty.getMinMonths())
                .thenReturn(2);
        when(mockValidPeriodProperty.getMaxMonths())
                .thenReturn(10);
        when(mockUserRepository.findById(eq(UUID.fromString(createCardRequest.getOwnerId()))))
                        .thenReturn(Optional.empty());

        createCardRequest.setValidThru(new ValidThruRequest(currentTime.getYear(), currentTime.getMonthValue()));

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> cardService.createCard(createCardRequest));
        assertEquals("Пользователь с ID: %s не найден".formatted(createCardRequest.getOwnerId()),
                ex.getMessage());

        verify(mockValidPeriodProperty, times(1))
                .getMinMonths();
        verify(mockValidPeriodProperty, times(1))
                .getMaxMonths();
        verify(mockCardRepository, never())
                .save(any(Card.class));
        verify(mockUserRepository)
                .findById(any(UUID.class));
    }

    @Test
    void createCard_validRequest_expect_Success() {
        LocalDateTime currentTime = LocalDateTime.now().plusMonths(4);
        createCardRequest.setValidThru(new ValidThruRequest(currentTime.getYear(), currentTime.getMonthValue()));
        when(mockValidPeriodProperty.getMinMonths())
                .thenReturn(2);
        when(mockValidPeriodProperty.getMaxMonths())
                .thenReturn(10);
        when(mockUserRepository.findById(eq(UUID.fromString(createCardRequest.getOwnerId()))))
                .thenReturn(Optional.of(user));
        when(mockBasicTextEncryptor.decrypt(anyString()))
                .thenReturn(cardNumber);
        when(mockBasicTextEncryptor.encrypt(anyString()))
                .thenReturn(cardNumber);
        when(mockCardRepository.save(any(Card.class)))
                .thenReturn(card);

        cardService.createCard(createCardRequest);

        verify(mockValidPeriodProperty, times(1))
                .getMinMonths();
        verify(mockValidPeriodProperty, times(1))
                .getMaxMonths();
        verify(mockCardRepository)
                .save(any(Card.class));
        verify(mockUserRepository)
                .findById(any(UUID.class));
        verify(mockBasicTextEncryptor).decrypt(anyString());
        verify(mockBasicTextEncryptor).encrypt(anyString());
    }

    @Test
    void updateCard_cardDoesNotExists_expect_entityNotFoundException() {
        when(mockCardRepository.findById(eq(cardId)))
                .thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> cardService.updateCard(cardId, updateCardRequest));
        assertEquals("Карта с ID: %s не найдена".formatted(cardId), ex.getMessage());

        verify(mockCardRepository)
                .findById(eq(cardId));
        verify(mockValidPeriodProperty, never())
                .getMinMonths();
        verify(mockValidPeriodProperty, never())
                .getMaxMonths();
        verify(mockCardRepository, never())
                .save(any(Card.class));
    }

    @Test
    void updateCard_ValidThruIsLessThanAllowedValue_expect_bankCardsException() {
        LocalDateTime currentTime = LocalDateTime.now();
        when(mockCardRepository.findById(eq(cardId)))
                .thenReturn(Optional.of(card));
        when(mockValidPeriodProperty.getMinMonths())
                .thenReturn(1);

        updateCardRequest.setNewValidThru(new ValidThruRequest(currentTime.getYear(), currentTime.getMonthValue()));

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> cardService.updateCard(cardId, updateCardRequest));
        assertEquals("Срок действия карты не может быть меньше %d месяцев".formatted(mockValidPeriodProperty.getMinMonths()),
                ex.getMessage());

        verify(mockCardRepository)
                .findById(eq(cardId));
        verify(mockValidPeriodProperty, times(3))
                .getMinMonths();
        verify(mockValidPeriodProperty, never())
                .getMaxMonths();
        verify(mockCardRepository, never())
                .save(any(Card.class));
    }

    @Test
    void updateCard_ValidThruIsGreaterThanAllowedValue_expect_bankCardsException() {
        LocalDateTime currentTime = LocalDateTime.now().plusMonths(4);
        when(mockCardRepository.findById(eq(cardId)))
                .thenReturn(Optional.of(card));
        when(mockValidPeriodProperty.getMinMonths())
                .thenReturn(2);
        when(mockValidPeriodProperty.getMaxMonths())
                .thenReturn(3);

        updateCardRequest.setNewValidThru(new ValidThruRequest(currentTime.getYear(), currentTime.getMonthValue()));

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> cardService.updateCard(cardId, updateCardRequest));
        assertEquals("Срок действия карты не может быть больше %d месяцев".formatted(mockValidPeriodProperty.getMaxMonths()),
                ex.getMessage());

        verify(mockCardRepository)
                .findById(eq(cardId));
        verify(mockValidPeriodProperty)
                .getMinMonths();
        verify(mockValidPeriodProperty, times(3))
                .getMaxMonths();
        verify(mockCardRepository, never())
                .save(any(Card.class));
    }

    @Test
    void updateCard_validRequest_expect_Success() {
        LocalDateTime currentTime = LocalDateTime.now().plusMonths(4);
        when(mockCardRepository.findById(eq(cardId)))
                .thenReturn(Optional.of(card));
        createCardRequest.setValidThru(new ValidThruRequest(currentTime.getYear(), currentTime.getMonthValue()));
        when(mockValidPeriodProperty.getMinMonths())
                .thenReturn(2);
        when(mockValidPeriodProperty.getMaxMonths())
                .thenReturn(10);
        when(mockCardRepository.save(any(Card.class)))
                .thenReturn(card);
        when(mockBasicTextEncryptor.decrypt(anyString()))
                .thenReturn(cardNumber);

        updateCardRequest.setNewValidThru(new ValidThruRequest(currentTime.getYear(), currentTime.getMonthValue()));
        cardService.updateCard(cardId, updateCardRequest);

        verify(mockCardRepository)
                .findById(eq(cardId));
        verify(mockValidPeriodProperty)
                .getMinMonths();
        verify(mockValidPeriodProperty)
                .getMaxMonths();
        verify(mockCardRepository)
                .save(any(Card.class));
        verify(mockBasicTextEncryptor).decrypt(anyString());
    }

    @Test
    void deleteCard_cardDoesNotExist_expect_Success() {
        when(mockCardRepository.findById(cardId))
                .thenReturn(Optional.empty());

        cardService.deleteCard(cardId);

        verify(mockCardRepository)
                .findById(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .deleteByCardId(eq(cardId));
        verify(mockCardRepository, never())
                .deleteById(cardId);
    }

    @Test
    void deleteCard_cardExists_expect_Success() {
        when(mockCardRepository.findById(cardId))
                .thenReturn(Optional.of(card));

        cardService.deleteCard(cardId);

        verify(mockCardRepository)
                .findById(eq(cardId));
        verify(mockOrderForLockRepository)
                .deleteByCardId(eq(cardId));
        verify(mockCardRepository)
                .deleteById(cardId);
    }
}