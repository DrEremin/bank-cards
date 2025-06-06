package com.example.bankcards.util.property;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "bank-cards.valid-period")
public class ValidPeriodProperty {

    @NotNull(message = "Свойство bank-cards.valid-period.min-days не может быть null")
    @Positive(message = "Свойство bank-cards.valid-period.min-days не может быть отрицательным")
    Integer minMonths;

    @NotNull(message = "Свойство bank-cards.valid-period.max-days не может быть null")
    Integer maxMonths;
}
