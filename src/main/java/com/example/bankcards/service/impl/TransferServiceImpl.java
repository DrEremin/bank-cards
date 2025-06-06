package com.example.bankcards.service.impl;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.util.mapper.TransferResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TransferResponse createTransfer(TransferRequest request) {
        UUID sourceCardId = UUID.fromString(request.getSourceCardId());
        UUID targetCardId = UUID.fromString(request.getTargetCardId());

        Card sourceCard = cardRepository.findById(sourceCardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с ID: %s не найдена".formatted(sourceCardId)));
        Card targetCard = cardRepository.findById(targetCardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с ID: %s не найдена".formatted(targetCardId)));

        BigDecimal amount = new BigDecimal(request.getAmount());
        BigDecimal sourceCardBalance = sourceCard.getBalance();

        if (amount.compareTo(sourceCardBalance) > 0) {
            throw new BankCardsException("Недостаточно средств на карте с ID: %s".formatted(sourceCardId));
        }

        sourceCard.setBalance(sourceCard.getBalance().subtract(amount).setScale(2, RoundingMode.HALF_UP));
        targetCard.setBalance(targetCard.getBalance().add(amount).setScale(2, RoundingMode.HALF_UP));

        cardRepository.save(sourceCard);
        cardRepository.save(targetCard);

        Transfer transfer = transferRepository.save(new Transfer(null, sourceCard, targetCard, amount));

        return TransferResponseMapper.fromTransfer(transfer);
    }
}
