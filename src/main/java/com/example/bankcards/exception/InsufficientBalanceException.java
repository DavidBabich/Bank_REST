package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(Long cardId, String currentBalance, String requiredAmount) {
        super(String.format("Недостаточный баланс на карте %d. Текущий баланс: %s, требуемая сумма: %s", 
                cardId, currentBalance, requiredAmount));
    }
} 