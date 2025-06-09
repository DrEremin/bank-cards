package com.example.bankcards.scheduler;

import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardScheduler {

    private final CardService cardService;

    @Async("expiredCardsStatusUpdateExecutor")
    @Scheduled(cron = "${bank-cards.scheduler.expired-cards-update-status.cron}")
    public void updateExpiredCardsStatus() {
        log.info("Запущена задача обновления статуса карт с истекшим сроком");
        Integer count = cardService.updateExpiredCardsStatus();
        log.info("Успешно обновлен статус на EXPIRED у {} карт", count);
    }
}
