package org.transferservice.client;


import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.transferservice.client.dto.AccountResponse;
import org.transferservice.client.dto.AccountTransactionRequest;

import java.math.BigDecimal;


@Component
public class RestClientAccountClient implements AccountClient{

    private final RestClient restClient;
    private final CircuitBreaker circuitBreaker;
    private final Logger log = LoggerFactory.getLogger(RestClientAccountClient.class);

    public RestClientAccountClient(@Qualifier("accountRestClient") RestClient restClient, CircuitBreaker circuitBreaker) {
        this.restClient = restClient;
        this.circuitBreaker = circuitBreaker;
    }

    @Retryable(
            includes = ResourceAccessException.class,
            maxRetries = 2,
            delay = 500
    )
    public AccountResponse getAccountById(Long accountId) {

        log.info("Calling Account Service for account ID: " + accountId);

        return restClient.get()
                        .uri("/api/accounts/{accountId}", accountId)
                        .retrieve()
                        .body(AccountResponse.class);
    }

    @Override
    public AccountResponse debitAccount(
            Long accountId,
            String operationId,
            BigDecimal amount) {

        AccountTransactionRequest request = new AccountTransactionRequest();
        request.setOperationId(operationId);
        request.setAmount(amount);

        return restClient.post()
                .uri("/api/accounts/{accountId}/debit", accountId)
                .body(request)
                .retrieve()
                .body(AccountResponse.class);

    }

    @Override
    public AccountResponse creditAccount(
            Long accountId,
            String operationId,
            BigDecimal amount) {

        AccountTransactionRequest request = new AccountTransactionRequest();
        request.setOperationId(operationId);
        request.setAmount(amount);

        return restClient.post()
                .uri("/api/accounts/{accountId}/credit", accountId)
                .body(request)
                .retrieve()
                .body(AccountResponse.class);
    }
}
