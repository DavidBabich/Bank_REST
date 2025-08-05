package com.example.bankcards.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String encryptedNumber; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner; 

    @Column(nullable = false)
    private LocalDate expiryDate; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status; 

    @Column(nullable = false)
    private BigDecimal balance; 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEncryptedNumber() { return encryptedNumber; }
    public void setEncryptedNumber(String encryptedNumber) { this.encryptedNumber = encryptedNumber; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public CardStatus getStatus() { return status; }
    public void setStatus(CardStatus status) { this.status = status; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}