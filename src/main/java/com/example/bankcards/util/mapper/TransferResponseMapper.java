package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.util.CardNumberMasker;

public class TransferResponseMapper {

    public static TransferResponse fromTransfer(Transfer transfer) {
        String sourceCardNumber = transfer.getSourceCard().getEncodedNumber();
        String targetCardNumber = transfer.getTargetCard().getEncodedNumber();

        return TransferResponse.builder()
                .id(transfer.getId().toString())
                .sourceCardNumber(CardNumberMasker.maskNumber(sourceCardNumber))
                .targetCardNUmber(CardNumberMasker.maskNumber(targetCardNumber))
                .amount(transfer.getAmount().toString())
                .timestamp(transfer.getCreateTime())
                .build();
    }
}
