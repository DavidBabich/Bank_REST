package com.example.bankcards.service.impl;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardNumberGenerator cardNumberGenerator;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CardNumberGenerator cardNumberGenerator) {
        this.cardRepository = cardRepository;
        this.cardNumberGenerator = cardNumberGenerator;
    }

    @Override
    public Card createCard(Card card) {
        if (card.getOwner() == null) {
            throw new IllegalArgumentException("Владелец карты обязателен");
        }
        if (card.getBalance() == null || card.getBalance().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Баланс карты не может быть отрицательным");
        }

        String cardNumber = cardNumberGenerator.generateCardNumber();
        String encryptedNumber = cardNumberGenerator.encryptCardNumber(cardNumber);
        card.setEncryptedNumber(encryptedNumber);
        
        return cardRepository.save(card);
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public Optional<Card> findByEncryptedNumber(String encryptedNumber) {
        return cardRepository.findByEncryptedNumber(encryptedNumber);
    }

    @Override
    public List<Card> findByOwner(User owner) {
        return cardRepository.findByOwner(owner);
    }

    @Override
    public Page<Card> findByOwner(User owner, Pageable pageable) {
        return cardRepository.findByOwner(owner, pageable);
    }

    @Override
    public Page<Card> findAll(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    @Override
    public Page<Card> findByStatus(CardStatus status, Pageable pageable) {
        return cardRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Card> findByOwnerAndStatus(User owner, CardStatus status, Pageable pageable) {
        return cardRepository.findByOwnerAndStatus(owner, status, pageable);
    }

    @Override
    public Card updateCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    @Override
    public Card changeStatus(Long cardId, CardStatus status) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setStatus(status);
        return cardRepository.save(card);
    }

    @Override
    public String maskCardNumber(String encryptedNumber) {
        // Заглушка:возвращает последние 4 символа, остальное маскирует
        if (encryptedNumber == null || encryptedNumber.length() < 4) return "****";
        return "**** **** **** " + encryptedNumber.substring(encryptedNumber.length() - 4);
    }
}