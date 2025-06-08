package com.example.bankcards.service.impl;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.orderforlock.CreateOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.UpdateOrderForLockRequest;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.UniquenessViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderForLockServiceImplTest extends AbstractTest {

    private final UUID id = UUID.randomUUID();
    private final UUID cardId = card.getId();
    private final CreateOrderForLockRequest createOrderForLockRequest = new CreateOrderForLockRequest(cardId.toString());

    private final OrderForLock order = new OrderForLock(UUID.randomUUID(), card, OrderForLockStatus.COMPLETED);

    @BeforeEach
    void beforeEach() {
        card.setOwner(user);
        card.setStatus(CardStatus.ACTIVE);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void createOrderForLock_cardDoesNotExist_expect_entityNotFoundException() {
        when(mockCardRepository.findByIdWithUser(eq(cardId)))
                .thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> orderForLockService.createOrderForLock(createOrderForLockRequest));
        assertEquals("Карта не найдена", ex.getMessage());

        verify(mockCardRepository)
                .findByIdWithUser(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .findByCardId(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .save(any(OrderForLock.class));
    }

    @Test
    void createOrderForLock_cardDoesNotBelongToUser_expect_bankCardsException() {
        card.setOwner(new User(UUID.randomUUID(), null, null, null, null));
        when(mockCardRepository.findByIdWithUser(eq(cardId)))
                .thenReturn(Optional.of(card));

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> orderForLockService.createOrderForLock(createOrderForLockRequest));
        assertEquals("Карта не принадлежит текущему пользователю", ex.getMessage());

        verify(mockCardRepository)
                .findByIdWithUser(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .findByCardId(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .save(any(OrderForLock.class));
    }

    @Test
    void createOrderForLock_cardIsNotActive_expect_bankCardsException() {
        card.setStatus(CardStatus.LOCKED);
        when(mockCardRepository.findByIdWithUser(eq(cardId)))
                .thenReturn(Optional.of(card));

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> orderForLockService.createOrderForLock(createOrderForLockRequest));
        assertEquals("Запрос на блокировку карты отклонен, карта уже не активна", ex.getMessage());

        verify(mockCardRepository)
                .findByIdWithUser(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .findByCardId(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .save(any(OrderForLock.class));
    }

    @Test
    void createOrderForLock_orderAlreadyExists_expect_uniquenessViolationException() {
        when(mockCardRepository.findByIdWithUser(eq(cardId)))
                .thenReturn(Optional.of(card));
        when(mockOrderForLockRepository.findByCardId(cardId))
                .thenReturn(Optional.of(order));

        Exception ex = assertThrowsExactly(UniquenessViolationException.class,
                () -> orderForLockService.createOrderForLock(createOrderForLockRequest));
        assertEquals("Запрос на блокировку карты отклонен, запрос уже был создан", ex.getMessage());

        verify(mockCardRepository)
                .findByIdWithUser(eq(cardId));
        verify(mockOrderForLockRepository)
                .findByCardId(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .save(any(OrderForLock.class));
    }

    @Test
    void createOrderForLock_NotErrors_expect_Success() {
        when(mockCardRepository.findByIdWithUser(eq(cardId)))
                .thenReturn(Optional.of(card));
        when(mockOrderForLockRepository.findByCardId(cardId))
                .thenReturn(Optional.empty());
        when(mockOrderForLockRepository.save(any(OrderForLock.class)))
                .thenReturn(new OrderForLock(id, card, OrderForLockStatus.PENDING));

        orderForLockService.createOrderForLock(createOrderForLockRequest);

        verify(mockCardRepository)
                .findByIdWithUser(eq(cardId));
        verify(mockOrderForLockRepository)
                .findByCardId(eq(cardId));
        verify(mockOrderForLockRepository)
                .save(any(OrderForLock.class));
    }

    @Test
    void updateOrderForLock_orderForLockNotFound_expect_entityNotFoundException() {
        var updateOrderForLockRequest = new UpdateOrderForLockRequest(OrderForLockStatus.COMPLETED.name());
        when(mockOrderForLockRepository.findById(eq(id)))
                .thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> orderForLockService.updateOrderForLock(id, updateOrderForLockRequest));
        assertEquals("Запрос на блокировку карты не найден", ex.getMessage());

        verify(mockOrderForLockRepository)
                .findById(eq(id));
        verify(mockOrderForLockRepository, never())
                .save(any(OrderForLock.class));
    }

    @Test
    void updateOrderForLock_orderForLockAlreadyNotPending_expect_bankCardsException() {
        var updateOrderForLockRequest = new UpdateOrderForLockRequest(OrderForLockStatus.COMPLETED.name());
        when(mockOrderForLockRepository.findById(eq(id)))
                .thenReturn(Optional.of(new OrderForLock(id, card, OrderForLockStatus.COMPLETED)));

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> orderForLockService.updateOrderForLock(id, updateOrderForLockRequest));
        assertEquals("Действие отклонено, потому что запрос на блокировку карты уже исполнен",
                ex.getMessage());

        verify(mockOrderForLockRepository)
                .findById(eq(id));
        verify(mockOrderForLockRepository, never())
                .save(any(OrderForLock.class));
    }

    @Test
    void updateOrderForLock_noErrors_expect_Success() {
        var updateOrderForLockRequest = new UpdateOrderForLockRequest(OrderForLockStatus.COMPLETED.name());
        OrderForLock orderForLock = new OrderForLock(id, card, OrderForLockStatus.PENDING);
        when(mockOrderForLockRepository.findById(eq(id)))
                .thenReturn(Optional.of(orderForLock));
        when(mockOrderForLockRepository.save(any(OrderForLock.class)))
                .thenReturn(orderForLock);

        orderForLockService.updateOrderForLock(id, updateOrderForLockRequest);

        assertEquals(OrderForLockStatus.COMPLETED, orderForLock.getStatus());

        verify(mockOrderForLockRepository)
                .findById(eq(id));
        verify(mockOrderForLockRepository)
                .save(any(OrderForLock.class));
    }
}