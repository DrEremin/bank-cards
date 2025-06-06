package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.ValidThruResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.CardNumberMasker;

import java.time.LocalDateTime;

public class CardResponseMapper {

    public static CardResponse fromCard(Card card) {
        String ownerId = card.getOwner().getId().toString();
        ValidThruResponse validThruResponse = buildValidThruResponse(card.getExpiredTime());
        String maskedNumber = CardNumberMasker.maskNumber(card.getEncodedNumber());
        return CardResponse.builder()
                .id(card.getId().toString())
                .ownerId(ownerId)
                .number(maskedNumber)
                .status(card.getStatus().name())
                .validThru(validThruResponse)
                .build();
    }

    private static ValidThruResponse buildValidThruResponse(LocalDateTime expiredTime) {
        String year = String.valueOf(expiredTime.getYear() - 2000);
        int monthValue = expiredTime.getMonthValue();
        String month = monthValue > 9 ? String.valueOf(monthValue) : "0" + monthValue;

        return ValidThruResponse.builder()
                .expireYear(year)
                .expireMonth(month)
                .build();
    }
}
