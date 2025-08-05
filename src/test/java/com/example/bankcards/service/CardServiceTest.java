package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    private User testUser;
    private Card testCard;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testCard = new Card();
        testCard.setId(1L);
        testCard.setOwner(testUser);
        testCard.setEncryptedNumber("encrypted1234567890123456");
        testCard.setExpiryDate(LocalDate.of(2025, 12, 31));
        testCard.setStatus(CardStatus.ACTIVE);
        testCard.setBalance(new BigDecimal("1000.00"));
    }

    @Test
    void testCreateCard() {
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        Card result = cardService.createCard(testCard);

        assertNotNull(result);
        assertEquals(testCard.getId(), result.getId());
        assertEquals(testCard.getStatus(), result.getStatus());
    }

    @Test
    void testFindById() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));

        Optional<Card> result = cardService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testCard.getId(), result.get().getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(cardRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Card> result = cardService.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testChangeStatus() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        Card result = cardService.changeStatus(1L, CardStatus.BLOCKED);

        assertNotNull(result);
        assertEquals(CardStatus.BLOCKED, result.getStatus());
    }

    @Test
    void testMaskCardNumber() {
        String masked = cardService.maskCardNumber("encrypted1234567890123456");
        
        assertEquals("**** **** **** 3456", masked);
    }

    @Test
    void testMaskCardNumberNull() {
        String masked = cardService.maskCardNumber(null);
        
        assertEquals("****", masked);
    }
} 