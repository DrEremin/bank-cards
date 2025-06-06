package com.example.bankcards.service.impl;

import com.example.bankcards.dto.card.*;
import com.example.bankcards.dto.common.PageableRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.RoleName;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.OrderForLockRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.mapper.CardResponseMapper;
import com.example.bankcards.util.mapper.PageableMapper;
import com.example.bankcards.util.property.ValidPeriodProperty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    public static final int LENGTH_CARD_NUMBER = 16;

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ValidPeriodProperty validPeriodProperty;
    private final OrderForLockRepository orderForLockRepository;
    private final BasicTextEncryptor basicTextEncryptor;

    @Override
    @Transactional
    public CardResponse createCard(CreateCardRequest request) {
        ValidThruRequest validThru = request.getValidThru();
        LocalDateTime expiredTime = createExpiredTime(validThru.getExpiryYear(), validThru.getExpiryMonth());
        String ownerId = request.getOwnerId();

        User owner = userRepository.findById(UUID.fromString(ownerId))
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID: %s не найден".formatted(ownerId)));

        boolean isOwnerHavaUserRole = owner.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .anyMatch(r -> r.equals(RoleName.ROLE_USER.name()));

        if (!isOwnerHavaUserRole) {
            throw new BankCardsException("Ошибка при создании карты. Пользователь %s не является USER"
                    .formatted(owner.getUserName()));
        }

        Card card = cardRepository.save(buildCard(owner, expiredTime));

        return CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber()));
    }

    @Override
    @Transactional
    public CardResponse updateCard(UUID id, UpdateCardRequest request) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Карта с ID: %s не найдена".formatted(id)));
        ValidThruRequest newValidThru = request.getNewValidThru();

        if (newValidThru != null) {
            LocalDateTime expiredTime = createExpiredTime(newValidThru.getExpiryYear(), newValidThru.getExpiryMonth());
            card.setExpiredTime(expiredTime);
        }

        if (request.getNewStatus() != null) {
            card.setStatus(CardStatus.valueOf(request.getNewStatus()));
        }

        card = cardRepository.save(card);

        return CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber()));
    }

    @Override
    @Transactional
    public void deleteCard(UUID cardId) {
        cardRepository.findById(cardId)
                .ifPresent(card -> {
                    orderForLockRepository.deleteByCardId(cardId);
                    cardRepository.deleteById(cardId);
                });
    }

    public List<CardResponse> findAllByUserId(UUID userId, Pageable pageable) {
        return cardRepository.findAllByUserId(userId, pageable).stream()
                .map(card -> CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber())))
                .toList();
    }

    @Override
    public List<CardResponse> findByFilter(FilterCardRequest request) {
        PageableRequest page = request.getPage();
        Pageable pageable = PageableMapper.mapPageable(page);
        UUID ownerId = null;
        List<CardStatus> cardStatuses = null;

        if (request.getOwnerId() != null) {
            ownerId = UUID.fromString(request.getOwnerId());
        }

        if (request.getStatuses() != null) {
            cardStatuses = request.getStatuses().stream()
                    .map(it -> CardStatus.valueOf(it.getStatus()))
                    .toList();
        }

        return cardRepository.findByFilter(
                        ownerId,
                        request.getCreateTimeFrom(),
                        request.getCreateTimeTo(),
                        request.getExpiredTimeForm(),
                        request.getExpiredTimeTo(),
                        cardStatuses,
                        pageable).stream()
                .map(card -> CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber())))
                .toList();
    }

    public CardResponse findById(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Карта с ID: %s не найдена".formatted(id)));

        return CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber()));
    }

    public BalanceResponse getBalanceById(UUID cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с ID: %s не найдена".formatted(cardId)));

        return new BalanceResponse(card.getBalance().toString());
    }

    private LocalDateTime createExpiredTime(Integer year, Integer month) {
        YearMonth expiredYearMonth = YearMonth.of(year, month);
        YearMonth currentYearMonth = YearMonth.now();

        if (expiredYearMonth.isBefore(currentYearMonth.plusMonths(validPeriodProperty.getMinMonths()))) {
            throw new BankCardsException("Срок действия карты не может быть меньше %d месяцев"
                    .formatted(validPeriodProperty.getMinMonths()));
        }

        if (expiredYearMonth.isAfter(currentYearMonth.plusMonths(validPeriodProperty.getMaxMonths()))) {
            throw new BankCardsException("Срок действия карты не может быть больше %d месяцев"
                    .formatted(validPeriodProperty.getMaxMonths()));
        }

        return LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
    }

    private Card buildCard(User owner, LocalDateTime expiredTime) {
        RandomStringUtils randomStringUtils = RandomStringUtils.secure();
        String cardNumber = randomStringUtils.nextNumeric(LENGTH_CARD_NUMBER);
        String encodedCardNumber = basicTextEncryptor.encrypt(cardNumber);

        return Card.builder()
                .encodedNumber(encodedCardNumber)
                .expiredTime(expiredTime)
                .owner(owner)
                .status(CardStatus.ACTIVE)
                .balance(new BigDecimal("0.00"))
                .build();
    }
}
