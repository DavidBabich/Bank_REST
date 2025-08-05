package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateCardRequest {
    @NotNull(message = "ID владельца обязателен")
    private Long ownerId; 

    @NotNull(message = "Срок действия обязателен")
    private LocalDate expiryDate; 

    @NotNull(message = "Начальный баланс обязателен")
    @Positive(message = "Баланс должен быть положительным")
    private BigDecimal initialBalance; 

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public BigDecimal getInitialBalance() { return initialBalance; }
    public void setInitialBalance(BigDecimal initialBalance) { this.initialBalance = initialBalance; }
} 