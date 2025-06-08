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
import com.example.bankcards.util.CardNumberMasker;
import com.example.bankcards.util.mapper.TransferResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final BasicTextEncryptor basicTextEncryptor;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TransferResponse createTransfer(TransferRequest request) {
        UUID sourceCardId = UUID.fromString(request.getSourceCardId());
        UUID targetCardId = UUID.fromString(request.getTargetCardId());
        List<Card> cards = findCards(sourceCardId, targetCardId);
        Card sourceCard = cards.get(0);
        Card targetCard = cards.get(1);

        BigDecimal amount = new BigDecimal(request.getAmount());
        BigDecimal sourceCardBalance = sourceCard.getBalance();

        if (amount.compareTo(sourceCardBalance) > 0) {
            throw new BankCardsException("Недостаточно средств на карте");
        }

        sourceCard.setBalance(sourceCard.getBalance().subtract(amount).setScale(2, RoundingMode.HALF_UP));
        targetCard.setBalance(targetCard.getBalance().add(amount).setScale(2, RoundingMode.HALF_UP));

        cardRepository.save(sourceCard);
        cardRepository.save(targetCard);

        Transfer transfer = transferRepository.save(new Transfer(null, sourceCard, targetCard, amount));
        TransferResponse response = TransferResponseMapper.fromTransfer(transfer);
        log.info("Операция перевода средств с карты {} на карту {} в размере {} выполнена успешно",
                sourceCardId,
                targetCardId,
                amount
        );

        return response;
    }

    private List<Card> findCards(UUID id1, UUID id2) {
        List<Card> cards = new ArrayList<>(2);
        cards.add(cardRepository.findByIdWithUser(id1)
                .orElseThrow(() -> new EntityNotFoundException("Карта-источник не найдена")));
        cards.add(cardRepository.findByIdWithUser(id2)
                .orElseThrow(() -> new EntityNotFoundException("Карта назначения не найдена")));
        UUID userId = getAuthorizedUserId();

        cards.forEach(card -> {
            if (!userId.equals(card.getOwner().getId())) {
                String cardNumber = CardNumberMasker.maskNumber(basicTextEncryptor.decrypt(card.getEncodedNumber()));
                throw new BankCardsException("Операция отклонена. Карта %s не принадлежит текущему пользователю"
                        .formatted(cardNumber)
                );
            }
        });

        return cards;
    }
}
