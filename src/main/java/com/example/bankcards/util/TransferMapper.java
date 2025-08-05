package com.example.bankcards.util;

import com.example.bankcards.entity.Transfer;
import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {
    private final CardService cardService;

    @Autowired
    public TransferMapper(CardService cardService) {
        this.cardService = cardService;
    }

    public TransferDto toDto(Transfer transfer) {
        TransferDto dto = new TransferDto();
        dto.setId(transfer.getId());
        dto.setFromCardMaskedNumber(cardService.maskCardNumber(transfer.getFromCard().getEncryptedNumber()));
        dto.setToCardMaskedNumber(cardService.maskCardNumber(transfer.getToCard().getEncryptedNumber()));
        dto.setAmount(transfer.getAmount());
        dto.setCreatedAt(transfer.getCreatedAt());
        return dto;
    }
} 