package org.transferservice.client;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.transferservice.client.dto.AccountResponse;

import java.math.BigDecimal;


@Component
public class RestClientAccountClient implements AccountClient{

    private final RestClient restClient;

    public RestClientAccountClient(@Qualifier("accountRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public AccountResponse getAccountById(Long accountId) {
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
                .uri("api/accounts/{accountId}/credit", accountId)
                .body(amount)
                .retrieve()
                .body(AccountResponse.class);
    }
}
