package org.transferservice.client;

import org.transferservice.client.dto.AccountResponse;

import java.math.BigDecimal;

public interface AccountClient {


    AccountResponse getAccountById(Long accountId);

    AccountResponse debitAccount(Long accountId, String operationId, BigDecimal amount);

    AccountResponse creditAccount(Long accountId, String operationId, BigDecimal amount);
}
