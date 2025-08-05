package com.example.bankcards.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferDto {
    private Long id;
    private String fromCardMaskedNumber; 
    private String toCardMaskedNumber; 
    private BigDecimal amount; 
    private LocalDateTime createdAt; 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFromCardMaskedNumber() { return fromCardMaskedNumber; }
    public void setFromCardMaskedNumber(String fromCardMaskedNumber) { this.fromCardMaskedNumber = fromCardMaskedNumber; }
    public String getToCardMaskedNumber() { return toCardMaskedNumber; }
    public void setToCardMaskedNumber(String toCardMaskedNumber) { this.toCardMaskedNumber = toCardMaskedNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 