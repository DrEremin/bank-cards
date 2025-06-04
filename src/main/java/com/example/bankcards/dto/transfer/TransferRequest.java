package com.example.bankcards.dto.transfer;

import com.example.bankcards.util.validation.ValidUUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull(message = "Идентификатор карты-источника не должен быть null")
    @ValidUUID(message = "Идентификатор карты-источника не соотетствует формату UUID")
    private String sourceCardId;

    @NotNull(message = "Идентификатор карты назначения не должен быть null")
    @ValidUUID(message = "Идентификатор карты назначения не соотетствует формату UUID")
    private String targetCardId;

    @NotNull(message = "Сумма перевода не должна быть null")
    @Pattern(regexp = "[0-9]{1,18}\\.[0-9]{2}", message = "Некорректный формат суммы перевода")
    private String amount;
}
