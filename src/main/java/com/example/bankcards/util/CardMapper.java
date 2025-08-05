package com.example.bankcards.util;

import com.example.bankcards.entity.Card;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {
    private final CardService cardService;

    @Autowired
    public CardMapper(CardService cardService) {
        this.cardService = cardService;
    }

    public CardDto toDto(Card card) {
        CardDto dto = new CardDto();
        dto.setId(card.getId());
        dto.setMaskedNumber(cardService.maskCardNumber(card.getEncryptedNumber()));
        dto.setOwnerUsername(card.getOwner().getUsername());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setStatus(card.getStatus());
        dto.setBalance(card.getBalance());
        return dto;
    }
} 