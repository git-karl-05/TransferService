package org.transferservice.client.dto;

import java.math.BigDecimal;

public class AccountTransactionRequest {

    private String operationId;
    private BigDecimal amount;

    public AccountTransactionRequest(){}

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
