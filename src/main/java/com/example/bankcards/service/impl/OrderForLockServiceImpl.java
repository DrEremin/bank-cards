package com.example.bankcards.service.impl;

import com.example.bankcards.dto.orderforlock.CreateOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.FilterOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.OrderForLockResponse;
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
import com.example.bankcards.service.OrderForLockService;
import com.example.bankcards.util.mapper.OrderForLockResponseMapper;
import com.example.bankcards.util.mapper.PageableMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderForLockServiceImpl implements OrderForLockService {

    private final OrderForLockRepository orderForLockRepository;
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public OrderForLockResponse createOrderForLock(CreateOrderForLockRequest request) {
        UUID cardId = UUID.fromString(request.getCardId());
        Card card = cardRepository.findByIdWithUser(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта не найдена"));

        if (!card.getOwner().getId().equals(getAuthorizedUserId())) {
            throw new BankCardsException("Карта не принадлежит текущему пользователю");
        }

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new BankCardsException("Запрос на блокировку карты отклонен, карта уже не активна");
        }

        orderForLockRepository.findByCardId(cardId)
                .ifPresent(order -> {
                    throw new UniquenessViolationException("Запрос на блокировку карты отклонен, запрос уже был создан");
                });

        OrderForLock orderForLock = new OrderForLock(null, card, OrderForLockStatus.PENDING);
        OrderForLockResponse response = OrderForLockResponseMapper.fromOrderForLock(orderForLockRepository
                .save(orderForLock)
        );
        log.info("Создание запроса на блокировку карты с ID: {} для пользователя с ID {} выполнено успешно",
                cardId,
                getAuthorizedUserId()
        );

        return response;
    }

    @Override
    @Transactional
    public OrderForLockResponse updateOrderForLock(UUID id, UpdateOrderForLockRequest request) {
        OrderForLock order = orderForLockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запрос на блокировку карты не найден"));

        if (order.getStatus() != OrderForLockStatus.PENDING) {
            throw new BankCardsException("Действие отклонено, потому что запрос на блокировку карты уже исполнен");
        }

        OrderForLockStatus newStatus = OrderForLockStatus.valueOf(request.getNewStatus());
        order.setStatus(newStatus);
        OrderForLockResponse response = OrderForLockResponseMapper.fromOrderForLock(orderForLockRepository.save(order));
        log.info("Обновление запроса с ID: {} на блокировку карты выполнено успешно пользователем с ID: {}",
                id,
                getAuthorizedUserId()
        );

        return response;
    }

    @Override
    public List<OrderForLockResponse> findByFilter(FilterOrderForLockRequest request) {
        Pageable pageable = PageableMapper.mapPageable(request.getPage());
        UUID cardId = null;
        List<OrderForLockStatus> statuses = null;

        if (request.getCardId() != null) {
            cardId = UUID.fromString(request.getCardId());
        }

        if (request.getStatuses() != null) {
            statuses = request.getStatuses().stream()
                    .map(it -> OrderForLockStatus.valueOf(it.getStatus()))
                    .toList();
        }

        List<OrderForLockResponse> responseList = orderForLockRepository.findByFilter(
                        cardId,
                        statuses,
                        request.getCreateTimeFrom(),
                        request.getCreateTimeTo(),
                        pageable)
                .stream()
                .map(OrderForLockResponseMapper::fromOrderForLock)
                .toList();
        log.info("Запрос на получение списка заявок на блокировки карт выполнен успено пользователем с ID:{}",
                getAuthorizedUserId()
        );

        return responseList;
    }

    @Override
    public OrderForLockResponse getOrderForLock(UUID id) {
        OrderForLock order = orderForLockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запрос на блокировку карты не найден"));
        OrderForLockResponse response = OrderForLockResponseMapper.fromOrderForLock(order);
        log.info("Запрос на получение данных заявки {} на блокироку карты для пользоателя с ID {} выполнен успешно",
                id,
                getAuthorizedUserId()
        );

        return response;
    }
}
