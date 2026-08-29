package org.transferservice.dto;

import org.transferservice.entity.TransferEntity;
import org.transferservice.entity.TransferStatus;

import java.math.BigDecimal;

public class TransferResponse {

    private Long transferId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String description;
    private TransferStatus status;

    public TransferResponse() {}

    public TransferResponse(TransferEntity transferEntity) {
        this.transferId = transferEntity.getTransferId();
        this.fromAccountId = transferEntity.getFromAccountId();
        this.toAccountId = transferEntity.getToAccountId();
        this.amount = transferEntity.getAmount();
        this.description = transferEntity.getDescription();
        this.status = transferEntity.getStatus();
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
}
