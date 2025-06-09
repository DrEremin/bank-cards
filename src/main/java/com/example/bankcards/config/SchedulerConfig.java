package com.example.bankcards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.*;

@EnableAsync
@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public ExecutorService expiredCardsStatusUpdateExecutor() {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(1);
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
        return new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, blockingQueue, handler);
    }
}
