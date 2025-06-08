package com.example.bankcards.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AdminInitializerService adminInitializerService;

    @Override
    public void run(String[] args) {
        adminInitializerService.initialize();
    }
}
