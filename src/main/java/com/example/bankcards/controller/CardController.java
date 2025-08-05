package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.CardMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;
    private final UserService userService;
    private final CardMapper cardMapper;

    @Autowired
    public CardController(CardService cardService, UserService userService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.userService = userService;
        this.cardMapper = cardMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CreateCardRequest request) {
        User owner = userService.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        
        Card card = new Card();
        card.setOwner(owner);
        card.setExpiryDate(request.getExpiryDate());
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(request.getInitialBalance());
        
        Card savedCard = cardService.createCard(card);
        return ResponseEntity.ok(cardMapper.toDto(savedCard));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCard(@PathVariable Long id, Authentication authentication) {
        Card card = cardService.findById(id).orElse(null);
        if (card == null) return ResponseEntity.notFound().build();
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
            card.getOwner().getUsername().equals(authentication.getName())) {
            return ResponseEntity.ok(cardMapper.toDto(card));
        }
        return ResponseEntity.status(403).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<Page<CardDto>> getCards(
            @RequestParam(required = false) String ownerUsername,
            @RequestParam(required = false) CardStatus status,
            Pageable pageable,
            Authentication authentication) {
        
        Page<Card> cards;
        
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // админ видит всё
            if (ownerUsername != null && status != null) {
                // Фильтрация по владельцу и статусу
                User owner = userService.findByUsername(ownerUsername)
                        .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
                cards = cardService.findByOwnerAndStatus(owner, status, pageable);
            } else if (ownerUsername != null) {
                User owner = userService.findByUsername(ownerUsername)
                        .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
                cards = cardService.findByOwner(owner, pageable);
            } else if (status != null) {
                cards = cardService.findByStatus(status, pageable);
            } else {
                // Без фильтров 
                cards = cardService.findAll(pageable);
            }
        } else {
            // user видит только свои карты
            ownerUsername = authentication.getName();
            if (ownerUsername != null && status != null) {
                User owner = userService.findByUsername(ownerUsername)
                        .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
                cards = cardService.findByOwnerAndStatus(owner, status, pageable);
            } else if (ownerUsername != null) {
                User owner = userService.findByUsername(ownerUsername)
                        .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
                cards = cardService.findByOwner(owner, pageable);
            } else if (status != null) {
                cards = cardService.findByStatus(status, pageable);
            } else {
                cards = cardService.findAll(pageable);
            }
        }
        
        Page<CardDto> cardDtos = cards.map(cardMapper::toDto);
        return ResponseEntity.ok(cardDtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<CardDto> changeCardStatus(
            @PathVariable Long id,
            @RequestParam CardStatus status) {
        
        Card updatedCard = cardService.changeStatus(id, status);
        return ResponseEntity.ok(cardMapper.toDto(updatedCard));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/block-request")
    public ResponseEntity<CardDto> requestBlockCard(@PathVariable Long id, Authentication authentication) {
        Card card = cardService.findById(id).orElse(null);
        if (card == null) return ResponseEntity.notFound().build();
        if (!card.getOwner().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(403).build();
        }
        Card updatedCard = cardService.changeStatus(id, CardStatus.BLOCKED);
        return ResponseEntity.ok(cardMapper.toDto(updatedCard));
    }
} 