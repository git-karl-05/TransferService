package org.transferservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.transferservice.client.dto.AccountResponse;

@Component
public class WebClientAccountClient {

    private final WebClient webClient;

    public WebClientAccountClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public AccountResponse getAccountByAccountId(Long accountId) {
        return webClient.get()
                .uri("/api/accounts/{accountId}", accountId)
                .retrieve()
                .bodyToMono(AccountResponse.class)
                .block();
    }
}
