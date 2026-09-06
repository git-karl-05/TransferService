package org.transferservice.entity;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @Column(nullable = false)
    private Long fromAccountId;
    @Column(nullable = false)
    private Long toAccountId;
    @Column(nullable = false)
    private String idempotencyKey;
    @Column(nullable = false)
    private BigDecimal amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private BigDecimal sourceStartingBalance;
    private BigDecimal sourceEndingBalance;
    private BigDecimal destinationStartingBalance;
    private BigDecimal destinationEndingBalance;

    public TransferEntity() {}

    public TransferEntity(Long fromAccountId, Long toAccountId, BigDecimal amount, String description, TransferStatus status) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.description = description;
        this.status = status;
    }

    public TransferEntity(Long fromAccountId, Long toAccountId, String idempotencyKey, BigDecimal amount, String description, TransferStatus status, BigDecimal sourceStartingBalance, BigDecimal sourceEndingBalance, BigDecimal destinationStartingBalance, BigDecimal destinationEndingBalance) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.idempotencyKey = idempotencyKey;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.sourceStartingBalance = sourceStartingBalance;
        this.sourceEndingBalance = sourceEndingBalance;
        this.destinationStartingBalance = destinationStartingBalance;
        this.destinationEndingBalance = destinationEndingBalance;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public BigDecimal getSourceStartingBalance() {
        return sourceStartingBalance;
    }

    public void setSourceStartingBalance(BigDecimal sourceStartingBalance) {
        this.sourceStartingBalance = sourceStartingBalance;
    }

    public BigDecimal getSourceEndingBalance() {
        return sourceEndingBalance;
    }

    public void setSourceEndingBalance(BigDecimal sourceEndingBalance) {
        this.sourceEndingBalance = sourceEndingBalance;
    }

    public BigDecimal getDestinationStartingBalance() {
        return destinationStartingBalance;
    }

    public void setDestinationStartingBalance(BigDecimal destinationStartingBalance) {
        this.destinationStartingBalance = destinationStartingBalance;
    }

    public BigDecimal getDestinationEndingBalance() {
        return destinationEndingBalance;
    }

    public void setDestinationEndingBalance(BigDecimal destinationEndingBalance) {
        this.destinationEndingBalance = destinationEndingBalance;
    }
}
