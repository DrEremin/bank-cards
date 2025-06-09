package com.example.bankcards.service.impl;

import com.example.bankcards.dto.card.*;
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
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

        boolean isOwnerHaveRoleUser = owner.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .anyMatch(r -> r.equals(RoleName.ROLE_USER.name()));

        if (!isOwnerHaveRoleUser) {
            throw new BankCardsException("Ошибка при создании карты. Пользователь %s не является USER"
                    .formatted(owner.getUserName()));
        }

        Card card = cardRepository.save(buildCard(owner, expiredTime));
        CardResponse response = CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber()));
        log.info("Операция создания карты для пользователя с ID: {} выполнена успешно пользователем с ID: {}",
                ownerId,
                getAuthorizedUserId()
        );

        return response;
    }

    @Override
    @Transactional
    public CardResponse updateCard(UUID id, UpdateCardRequest request) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Карта не найдена"));
        ValidThruRequest newValidThru = request.getNewValidThru();

        if (newValidThru != null) {
            LocalDateTime expiredTime = createExpiredTime(newValidThru.getExpiryYear(), newValidThru.getExpiryMonth());
            card.setExpiredTime(expiredTime);
        }

        if (request.getNewStatus() != null) {
            card.setStatus(CardStatus.valueOf(request.getNewStatus()));
        }

        card = cardRepository.save(card);
        CardResponse response = CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber()));
        log.info("Операция обновления карты c ID {} выполнена успешно пользователем с ID {}",
                card.getId(),
                getAuthorizedUserId()
        );

        return response;
    }

    @Override
    @Transactional
    public void deleteCard(UUID cardId) {
        cardRepository.findById(cardId)
                .ifPresent(card -> {
                    orderForLockRepository.deleteByCardId(cardId);
                    cardRepository.deleteById(cardId);
                    log.info("Операция удаления карты с ID: {} выполена успешно пользователем с ID: {}",
                            card.getId(),
                            getAuthorizedUserId()
                    );
                });
    }

    public List<CardResponse> findAllByUserId(Pageable pageable) {
        UUID userId = getAuthorizedUserId();
        List<CardResponse> responseList = cardRepository.findAllByUserId(userId, pageable).stream()
                .map(card -> CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber())))
                .toList();
        log.info("Запрос на получение всех карт пользователя с ID: {} выполнен успешно", userId);

        return responseList;
    }

    @Override
    public List<CardResponse> findByFilter(FilterCardRequest request) {
        Pageable pageable = PageableMapper.mapPageable(request.getPage());
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

        List<CardResponse> responseList = cardRepository.findByFilter(
                        ownerId,
                        request.getCreateTimeFrom(),
                        request.getCreateTimeTo(),
                        request.getExpiredTimeForm(),
                        request.getExpiredTimeTo(),
                        cardStatuses,
                        pageable)
                .stream()
                .map(card -> CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber())))
                .toList();
        log.info("Запрос на получение списка карт выполнен успешно пользователем с ID: {}", getAuthorizedUserId());

        return responseList;
    }

    public CardResponse findById(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Карта не найдена"));
        CardResponse response = CardResponseMapper.fromCard(card, basicTextEncryptor.decrypt(card.getEncodedNumber()));
        log.info("Запрос на получение карты с ID {} для пользователя с ID {} выполнен успешно",
                card.getId(),
                getAuthorizedUserId()
        );

        return response;
    }

    public BalanceResponse getBalanceById(UUID cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта не найдена"));
        BalanceResponse response = new BalanceResponse(card.getBalance().toString());
        log.info("Запрос на получение баланса карты с ID {} для пользователя с ID {} выполнен успешно",
                card.getId(),
                getAuthorizedUserId()
        );

        return response;
    }

    @Transactional
    public Integer updateExpiredCardsStatus() {
        LocalDateTime currentTime = LocalDateTime.now();
        return cardRepository.updateCardsStatusLessThanExpiredTime(currentTime, CardStatus.EXPIRED);
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
