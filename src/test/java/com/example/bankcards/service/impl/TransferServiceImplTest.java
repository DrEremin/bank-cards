package com.example.bankcards.service.impl;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

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

    @Mock
    private TransferRepository mockTransferRepository;
    @Mock
    private CardRepository mockCardRepository;
    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    void createTransfer_sourceCardNotFound_expect_entityNotFoundException() {
        when(mockCardRepository.findById(eq(sourceCardId)))
                .thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> transferService.createTransfer(transferRequest));
        assertEquals("Карта с ID: %s не найдена".formatted(sourceCardId), ex.getMessage());

        verify(mockCardRepository)
                .findById(eq(sourceCardId));
        verify(mockCardRepository, never())
                .findById(eq(targetCardId));
        verify(mockCardRepository, never())
                .save(sourceCard);
        verify(mockCardRepository, never())
                .save(targetCard);
        verify(mockTransferRepository, never())
                .save(any(Transfer.class));
    }

    @Test
    void createTransfer_targetCardNotFound_expect_entityNotFoundException() {
        when(mockCardRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(sourceCard))
                .thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> transferService.createTransfer(transferRequest));
        assertEquals("Карта с ID: %s не найдена".formatted(targetCardId), ex.getMessage());

        verify(mockCardRepository)
                .findById(eq(sourceCardId));
        verify(mockCardRepository)
                .findById(eq(targetCardId));
        verify(mockCardRepository, never())
                .save(sourceCard);
        verify(mockCardRepository, never())
                .save(targetCard);
        verify(mockTransferRepository, never())
                .save(any(Transfer.class));
    }

    @Test
    void createTransfer_NotEnoughCashOnSourceCard_expect_bankCardsException() {
        when(mockCardRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(sourceCard))
                .thenReturn(Optional.of(targetCard));
        sourceCard.setBalance(amount.subtract(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP));

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> transferService.createTransfer(transferRequest));
        assertEquals("Недостаточно средств на карте с ID: %s".formatted(sourceCardId), ex.getMessage());

        verify(mockCardRepository)
                .findById(eq(sourceCardId));
        verify(mockCardRepository)
                .findById(eq(targetCardId));
        verify(mockCardRepository, never())
                .save(sourceCard);
        verify(mockCardRepository, never())
                .save(targetCard);
        verify(mockTransferRepository, never())
                .save(any(Transfer.class));
    }

    @Test
    void createTransfer_NotErrors_expect_Success() {
        when(mockCardRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(sourceCard))
                .thenReturn(Optional.of(targetCard));
        var transfer = new Transfer(UUID.randomUUID(), sourceCard, targetCard, amount);
        when(mockTransferRepository.save(any(Transfer.class)))
                .thenReturn(transfer);
        sourceCard.setBalance(amount.add(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP));
        targetCard.setBalance(BigDecimal.ZERO);

        transferService.createTransfer(transferRequest);

        verify(mockCardRepository)
                .findById(eq(sourceCardId));
        verify(mockCardRepository)
                .findById(eq(targetCardId));
        verify(mockCardRepository)
                .save(sourceCard);
        verify(mockCardRepository)
                .save(targetCard);
        verify(mockTransferRepository)
                .save(any(Transfer.class));
    }
}