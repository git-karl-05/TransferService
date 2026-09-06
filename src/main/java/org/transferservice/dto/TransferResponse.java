package org.transferservice.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.transferservice.entity.TransferEntity;
import org.transferservice.entity.TransferStatus;

import java.math.BigDecimal;

@JsonPropertyOrder({
        "transferId",
        "fromAccountId",
        "toAccountId",
        "amount",
        "description",
        "status",
        "sourceStartingBalance",
        "sourceEndingBalance",
        "destinationStartingBalance",
        "destinationEndingBalance"
})
public class TransferResponse {

    private Long transferId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String description;
    private TransferStatus status;

    private BigDecimal sourceStartingBalance;
    private BigDecimal sourceEndingBalance;

    private BigDecimal destinationStartingBalance;
    private BigDecimal destinationEndingBalance;

    public TransferResponse(){}


    public TransferResponse(Long transferId, Long fromAccountId, Long toAccountId, BigDecimal amount, TransferStatus status, BigDecimal sourceStartingBalance, BigDecimal sourceEndingBalance, BigDecimal destinationStartingBalance, BigDecimal destinationEndingBalance) {
        this.transferId = transferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
        this.sourceStartingBalance = sourceStartingBalance;
        this.sourceEndingBalance = sourceEndingBalance;
        this.destinationStartingBalance = destinationStartingBalance;
        this.destinationEndingBalance = destinationEndingBalance;
    }



    public TransferResponse(
            TransferEntity entity
    ) {

        this.transferId = entity.getTransferId();
        this.fromAccountId = entity.getFromAccountId();
        this.toAccountId = entity.getToAccountId();
        this.amount = entity.getAmount();
        this.description = entity.getDescription();
        this.status = entity.getStatus();

        this.sourceStartingBalance = entity.getSourceStartingBalance();
        this.sourceEndingBalance = entity.getSourceEndingBalance();
        this.destinationStartingBalance = entity.getDestinationStartingBalance();
        this.destinationEndingBalance = entity.getDestinationEndingBalance();
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
