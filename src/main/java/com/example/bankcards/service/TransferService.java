package com.example.bankcards.service;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;

public interface TransferService extends AuthorizedUserIdExtractor {

    TransferResponse createTransfer(TransferRequest request);
}
