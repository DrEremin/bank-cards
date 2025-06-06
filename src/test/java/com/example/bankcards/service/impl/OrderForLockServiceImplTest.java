package com.example.bankcards.service.impl;

import com.example.bankcards.dto.orderforlock.CreateOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.UpdateOrderForLockRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.OrderForLock;
import com.example.bankcards.entity.OrderForLockStatus;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.UniquenessViolationException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.OrderForLockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderForLockServiceImplTest {

    private final UUID id = UUID.randomUUID();
    private final UUID cardId = UUID.randomUUID();
    private final String cardNumber = "1234567890123456";
    private final CreateOrderForLockRequest createOrderForLockRequest = new CreateOrderForLockRequest(cardId.toString());
    private final Card card = Card.builder()
            .id(cardId)
            .encodedNumber(cardNumber)
            .build();
    private final OrderForLock order = new OrderForLock(UUID.randomUUID(), card, OrderForLockStatus.COMPLETED);

    @Mock
    private OrderForLockRepository mockOrderForLockRepository;
    @Mock
    private CardRepository mockCardRepository;
    @InjectMocks
    private OrderForLockServiceImpl orderForLockService;

    @Test
    void createOrderForLok_cardDoesNotExist_expect_entityNotFoundException() {
        when(mockCardRepository.findById(eq(cardId)))
                .thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> orderForLockService.createOrderForLok(createOrderForLockRequest));
        assertEquals("Карта с ID: %s не найдена".formatted(cardId), ex.getMessage());

        verify(mockCardRepository)
                .findById(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .findByCardId(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .save(any(OrderForLock.class));
    }

    @Test
    void createOrderForLok_orderAlreadyExists_expect_uniquenessViolationException() {
        when(mockCardRepository.findById(eq(cardId)))
                .thenReturn(Optional.of(card));
        when(mockOrderForLockRepository.findByCardId(cardId))
                .thenReturn(Optional.of(order));

        Exception ex = assertThrowsExactly(UniquenessViolationException.class,
                () -> orderForLockService.createOrderForLok(createOrderForLockRequest));
        assertEquals("Запрос на блокировку карты с ID: %s уже был создан".formatted(cardId), ex.getMessage());

        verify(mockCardRepository)
                .findById(eq(cardId));
        verify(mockOrderForLockRepository)
                .findByCardId(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .save(any(OrderForLock.class));
    }

    @Test
    void createOrderForLok_cardIsNotActive_expect_bankCardsException() {
        when(mockCardRepository.findById(eq(cardId)))
                .thenReturn(Optional.of(card));
        when(mockOrderForLockRepository.findByCardId(cardId))
                .thenReturn(Optional.empty());
        card.setStatus(CardStatus.LOCKED);

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> orderForLockService.createOrderForLok(createOrderForLockRequest));
        assertEquals("Запрос на блокировку карты с ID: %s отклонен, карта уже не активна".formatted(cardId),
                ex.getMessage());

        verify(mockCardRepository)
                .findById(eq(cardId));
        verify(mockOrderForLockRepository)
                .findByCardId(eq(cardId));
        verify(mockOrderForLockRepository, never())
                .save(any(OrderForLock.class));
    }

    @Test
    void createOrderForLok_NotErrors_expect_Success() {
        card.setStatus(CardStatus.ACTIVE);
        when(mockCardRepository.findById(eq(cardId)))
                .thenReturn(Optional.of(card));
        when(mockOrderForLockRepository.findByCardId(cardId))
                .thenReturn(Optional.empty());
        when(mockOrderForLockRepository.save(any(OrderForLock.class)))
                .thenReturn(new OrderForLock(id, card, OrderForLockStatus.PENDING));

        orderForLockService.createOrderForLok(createOrderForLockRequest);

        verify(mockCardRepository)
                .findById(eq(cardId));
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
        assertEquals("Запрос с ID: %s на блокировку карты не найден".formatted(id), ex.getMessage());

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
        assertEquals("Действие отклонено, потому что запрос с ID: %s на блокировку карты уже исполнен".formatted(id),
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