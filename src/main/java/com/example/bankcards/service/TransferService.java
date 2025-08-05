package com.example.bankcards.service;

import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.Card;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransferService {
    Transfer transferBetweenCards(Card from, Card to, BigDecimal amount);
    List<Transfer> findByCard(Card card);
    Optional<Transfer> findById(Long id);
    List<Transfer> findAll();
}