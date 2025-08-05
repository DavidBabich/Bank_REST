package com.example.bankcards.service.impl;

import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public Transfer transferBetweenCards(Card from, Card to, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительной");
        }
        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на карте отправителя");
        }
        // Списание и зачисление
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        Transfer transfer = new Transfer();
        transfer.setFromCard(from);
        transfer.setToCard(to);
        transfer.setAmount(amount);
        transfer.setCreatedAt(java.time.LocalDateTime.now());
        return transferRepository.save(transfer);
    }

    @Override
    public List<Transfer> findByCard(Card card) {
        return transferRepository.findByFromCardOrToCard(card, card);
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return transferRepository.findById(id);
    }

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }
}