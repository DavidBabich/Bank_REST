package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CardService {
    Card createCard(Card card);
    Optional<Card> findById(Long id);
    Optional<Card> findByEncryptedNumber(String encryptedNumber);
    List<Card> findByOwner(User owner);
    Page<Card> findByOwner(User owner, Pageable pageable);
    Page<Card> findAll(Pageable pageable);
    Page<Card> findByStatus(CardStatus status, Pageable pageable);
    Page<Card> findByOwnerAndStatus(User owner, CardStatus status, Pageable pageable);
    Card updateCard(Card card);
    void deleteCard(Long id);
    Card changeStatus(Long cardId, CardStatus status);
    String maskCardNumber(String encryptedNumber);
}