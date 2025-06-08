package com.example.bankcards.service.impl;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TransferServiceImplTest extends AbstractTest {

    private final UUID sourceCardId = UUID.randomUUID();
    private final UUID targetCardId = UUID.randomUUID();
    private final BigDecimal amount = new BigDecimal("200.55");
    private final String cardNumber = "1234567890123456";

    private final TransferRequest transferRequest = TransferRequest.builder()
            .sourceCardId(sourceCardId.toString())
            .targetCardId(targetCardId.toString())
            .amount(amount.toString())
            .build();

    private final Card sourceCard = Card.builder()
            .id(sourceCardId)
            .encodedNumber(cardNumber)
            .build();

    private final Card targetCard = Card.builder()
            .id(targetCardId)
            .encodedNumber(cardNumber)
            .build();

    @BeforeEach
    void beforeEach() {
        sourceCard.setOwner(user);
        targetCard.setOwner(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void createTransfer_sourceCardNotFound_expect_entityNotFoundException() {
        when(mockCardRepository.findByIdWithUser(eq(sourceCardId)))
                .thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> transferService.createTransfer(transferRequest)
        );
        assertEquals("Карта-источник не найдена", ex.getMessage());

        verify(mockCardRepository)
                .findByIdWithUser(eq(sourceCardId));
        verify(mockCardRepository, never())
                .findByIdWithUser(eq(targetCardId));
        verify(mockCardRepository, never())
                .save(sourceCard);
        verify(mockCardRepository, never())
                .save(targetCard);
        verify(mockTransferRepository, never())
                .save(any(Transfer.class));
    }

    @Test
    void createTransfer_targetCardNotFound_expect_entityNotFoundException() {
        when(mockCardRepository.findByIdWithUser(any(UUID.class)))
                .thenReturn(Optional.of(sourceCard))
                .thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> transferService.createTransfer(transferRequest)
        );
        assertEquals("Карта назначения не найдена", ex.getMessage());

        verify(mockCardRepository)
                .findByIdWithUser(eq(sourceCardId));
        verify(mockCardRepository)
                .findByIdWithUser(eq(targetCardId));
        verify(mockCardRepository, never())
                .save(sourceCard);
        verify(mockCardRepository, never())
                .save(targetCard);
        verify(mockTransferRepository, never())
                .save(any(Transfer.class));
    }

    @Test
    void createTransfer_sourceCardDoesNotBelongToUser_expect_bankCardsException() {
        sourceCard.setOwner(new User(UUID.randomUUID(), null, null, null, null));
        when(mockCardRepository.findByIdWithUser(any(UUID.class)))
                .thenReturn(Optional.of(sourceCard))
                .thenReturn(Optional.of(targetCard));
        when(mockBasicTextEncryptor.decrypt(anyString()))
                .thenReturn(cardNumber);

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> transferService.createTransfer(transferRequest)
        );
        assertAll(
                () -> assertTrue(ex.getMessage().startsWith("Операция отклонена. Карта")),
                () -> assertTrue(ex.getMessage().endsWith("не принадлежит текущему пользователю"))
        );

        verify(mockCardRepository)
                .findByIdWithUser(eq(sourceCardId));
        verify(mockCardRepository)
                .findByIdWithUser(eq(targetCardId));
        verify(mockCardRepository, never())
                .save(sourceCard);
        verify(mockCardRepository, never())
                .save(targetCard);
        verify(mockTransferRepository, never())
                .save(any(Transfer.class));
    }

    @Test
    void createTransfer_targetCardDoesNotBelongToUser_expect_bankCardsException() {
        targetCard.setOwner(new User(UUID.randomUUID(), null, null, null, null));
        when(mockCardRepository.findByIdWithUser(any(UUID.class)))
                .thenReturn(Optional.of(sourceCard))
                .thenReturn(Optional.of(targetCard));
        when(mockBasicTextEncryptor.decrypt(anyString()))
                .thenReturn(cardNumber);

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> transferService.createTransfer(transferRequest)
        );
        assertAll(
                () -> assertTrue(ex.getMessage().startsWith("Операция отклонена. Карта")),
                () -> assertTrue(ex.getMessage().endsWith("не принадлежит текущему пользователю"))
        );

        verify(mockCardRepository)
                .findByIdWithUser(eq(sourceCardId));
        verify(mockCardRepository)
                .findByIdWithUser(eq(targetCardId));
        verify(mockCardRepository, never())
                .save(sourceCard);
        verify(mockCardRepository, never())
                .save(targetCard);
        verify(mockTransferRepository, never())
                .save(any(Transfer.class));
    }

    @Test
    void createTransfer_NotEnoughCashOnSourceCard_expect_bankCardsException() {
        when(mockCardRepository.findByIdWithUser(any(UUID.class)))
                .thenReturn(Optional.of(sourceCard))
                .thenReturn(Optional.of(targetCard));
        sourceCard.setBalance(amount.subtract(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP));

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> transferService.createTransfer(transferRequest));
        assertEquals("Недостаточно средств на карте", ex.getMessage());

        verify(mockCardRepository)
                .findByIdWithUser(eq(sourceCardId));
        verify(mockCardRepository)
                .findByIdWithUser(eq(targetCardId));
        verify(mockCardRepository, never())
                .save(sourceCard);
        verify(mockCardRepository, never())
                .save(targetCard);
        verify(mockTransferRepository, never())
                .save(any(Transfer.class));
    }

    @Test
    void createTransfer_NotErrors_expect_Success() {
        when(mockCardRepository.findByIdWithUser(any(UUID.class)))
                .thenReturn(Optional.of(sourceCard))
                .thenReturn(Optional.of(targetCard));
        var transfer = new Transfer(UUID.randomUUID(), sourceCard, targetCard, amount);
        when(mockTransferRepository.save(any(Transfer.class)))
                .thenReturn(transfer);
        sourceCard.setBalance(amount.add(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP));
        targetCard.setBalance(BigDecimal.ZERO);

        transferService.createTransfer(transferRequest);

        verify(mockCardRepository)
                .findByIdWithUser(eq(sourceCardId));
        verify(mockCardRepository)
                .findByIdWithUser(eq(targetCardId));
        verify(mockCardRepository)
                .save(sourceCard);
        verify(mockCardRepository)
                .save(targetCard);
        verify(mockTransferRepository)
                .save(any(Transfer.class));
    }
}