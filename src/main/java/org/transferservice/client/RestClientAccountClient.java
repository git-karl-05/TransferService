package org.transferservice.client;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.transferservice.client.dto.AccountResponse;

import java.math.BigDecimal;

@Component
public class RestClientAccountClient {

    private final RestClient restClient;

    public RestClientAccountClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public AccountResponse getAccountById(Long accountId) {
        return restClient.get()
                .uri("/api/accounts/{accountId}", accountId)
                .retrieve()
                .body(AccountResponse.class);
    }

    public void debitAccount(Long accountId, BigDecimal amount) {
        restClient.post()
                .uri("/api/accounts/{accountId}/debit", accountId)
                .body(amount)
                .retrieve()
                .toBodilessEntity();
    }

    public void creditAccount(Long accountId, BigDecimal amount) {
        restClient.post()
                .uri("api/accounts/{accountId}", accountId)
                .body(amount)
                .retrieve()
                .toBodilessEntity();
    }
}
