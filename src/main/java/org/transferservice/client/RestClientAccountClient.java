package org.transferservice.client;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.transferservice.client.dto.AccountResponse;
import org.transferservice.config.RetryConfig;

import java.math.BigDecimal;


@Component
public class RestClientAccountClient implements AccountClient{

    private final RestClient restClient;

    public RestClientAccountClient(@Qualifier("accountRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Retryable(
            includes = ResourceAccessException.class,
            maxRetries = 2,
            delay = 500
    )
    public AccountResponse getAccountById(Long accountId) {

        System.out.println("Calling Account Service for account: " + accountId);

        return restClient.get()
                .uri("/api/accounts/{accountId}", accountId)
                .retrieve()
                .body(AccountResponse.class);
    }

    @Override
    public AccountResponse debitAccount(Long accountId, BigDecimal amount) {
        return restClient.post()
                .uri("/api/accounts/{accountId}/debit", accountId)
                .body(amount)
                .retrieve()
                .body(AccountResponse.class);

    }

    @Override
    public AccountResponse creditAccount(Long accountId, BigDecimal amount) {
        return restClient.post()
                .uri("/api/accounts/{accountId}/credit", accountId)
                .body(amount)
                .retrieve()
                .body(AccountResponse.class);
    }
}
