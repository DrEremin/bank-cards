package com.example.bankcards.service.impl;

import com.example.bankcards.dto.common.PageableRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderForLockServiceImpl implements OrderForLockService {

    private final OrderForLockRepository orderForLockRepository;
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public OrderForLockResponse createOrderForLok(CreateOrderForLockRequest request) {
        UUID cardId = UUID.fromString(request.getCardId());
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карта с ID: %s не найдена".formatted(cardId)));

        orderForLockRepository.findByCardId(cardId)
                .ifPresent(order -> {
                    throw new UniquenessViolationException(
                            "Запрос на блокировку карты с ID: %s уже был создан".formatted(cardId));
                });

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new BankCardsException(
                    "Запрос на блокировку карты с ID: %s отклонен, карта уже не активна".formatted(cardId));
        }

        OrderForLock orderForLock = new OrderForLock(null, card, OrderForLockStatus.PENDING);

        return OrderForLockResponseMapper.fromOrderForLock(orderForLockRepository.save(orderForLock));
    }

    @Override
    @Transactional
    public OrderForLockResponse updateOrderForLock(UUID id, UpdateOrderForLockRequest request) {
        OrderForLock order = orderForLockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Запрос с ID: %s на блокировку карты не найден".formatted(id)));

        if (order.getStatus() != OrderForLockStatus.PENDING) {
            String msg = "Действие отклонено, потому что запрос с ID: %s на блокировку карты уже исполнен".formatted(id);
            throw new BankCardsException(msg);
        }

        OrderForLockStatus newStatus = OrderForLockStatus.valueOf(request.getNewStatus());
        order.setStatus(newStatus);

        return OrderForLockResponseMapper.fromOrderForLock(orderForLockRepository.save(order));
    }

    @Override
    public List<OrderForLockResponse> findByFilter(FilterOrderForLockRequest request) {
        PageableRequest page = request.getPage();
        Pageable pageable = PageableMapper.mapPageable(page);
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

        return orderForLockRepository.findByFilter(
                        cardId,
                        statuses,
                        request.getCreateTimeFrom(),
                        request.getCreateTimeTo(),
                        pageable)
                .stream()
                .map(OrderForLockResponseMapper::fromOrderForLock)
                .toList();
    }

    @Override
    public OrderForLockResponse getOrderForLock(UUID id) {
        OrderForLock order = orderForLockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Запрос с ID: %s на блокировку карты не найден".formatted(id)));

        return OrderForLockResponseMapper.fromOrderForLock(order);
    }
}
