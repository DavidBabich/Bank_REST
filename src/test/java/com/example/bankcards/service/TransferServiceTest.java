package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
    @Mock
    private TransferRepository transferRepository;
    @InjectMocks
    private TransferServiceImpl transferService;
    private Card fromCard;
    private Card toCard;

    @BeforeEach
    void setUp() {
        fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setBalance(new BigDecimal("1000.00"));
        toCard = new Card();
        toCard.setId(2L);
        toCard.setBalance(new BigDecimal("500.00"));
    }

    @Test
    void testTransferSuccess() {
        when(transferRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        var transfer = transferService.transferBetweenCards(fromCard, toCard, new BigDecimal("200.00"));
        assertNotNull(transfer);
        assertEquals(new BigDecimal("800.00"), fromCard.getBalance());
        assertEquals(new BigDecimal("700.00"), toCard.getBalance());
    }

    @Test
    void testTransferInsufficientFunds() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            transferService.transferBetweenCards(fromCard, toCard, new BigDecimal("2000.00")));
        assertTrue(ex.getMessage().contains("Недостаточно средств"));
    }

    @Test
    void testTransferNegativeAmount() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            transferService.transferBetweenCards(fromCard, toCard, new BigDecimal("-10.00")));
        assertTrue(ex.getMessage().contains("Сумма перевода должна быть положительной"));
    }
}