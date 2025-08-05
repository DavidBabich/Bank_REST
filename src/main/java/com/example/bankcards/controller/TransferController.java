package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.util.TransferMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final TransferService transferService;
    private final CardService cardService;
    private final TransferMapper transferMapper;

    @Autowired
    public TransferController(TransferService transferService, CardService cardService, TransferMapper transferMapper) {
        this.transferService = transferService;
        this.cardService = cardService;
        this.transferMapper = transferMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<TransferDto> createTransfer(@Valid @RequestBody TransferRequest request, Authentication authentication) {
        Card fromCard = cardService.findById(request.getFromCardId()).orElseThrow();
        Card toCard = cardService.findById(request.getToCardId()).orElseThrow();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            String username = authentication.getName();
            if (!fromCard.getOwner().getUsername().equals(username) || !toCard.getOwner().getUsername().equals(username)) {
                return ResponseEntity.status(403).build();
            }
        }
        Transfer transfer = transferService.transferBetweenCards(fromCard, toCard, request.getAmount());
        return ResponseEntity.ok(transferMapper.toDto(transfer));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<TransferDto>> getTransfersByCard(@PathVariable Long cardId, Authentication authentication) {
        Card card = cardService.findById(cardId).orElseThrow();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !card.getOwner().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(403).build();
        }
        List<Transfer> transfers = transferService.findByCard(card);
        List<TransferDto> transferDtos = transfers.stream()
                .map(transferMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transferDtos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<TransferDto> getTransfer(@PathVariable Long id, Authentication authentication) {
        Transfer transfer = transferService.findById(id).orElse(null);
        if (transfer == null) return ResponseEntity.notFound().build();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        String username = authentication.getName();
        if (!isAdmin &&
            !(transfer.getFromCard().getOwner().getUsername().equals(username) ||
              transfer.getToCard().getOwner().getUsername().equals(username))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(transferMapper.toDto(transfer));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TransferDto>> getAllTransfers() {
        List<Transfer> transfers = transferService.findAll();
        List<TransferDto> transferDtos = transfers.stream()
                .map(transferMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transferDtos);
    }
} 