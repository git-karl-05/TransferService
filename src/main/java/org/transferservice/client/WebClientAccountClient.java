package org.transferservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.transferservice.client.dto.AccountResponse;

import java.math.BigDecimal;

//@Component
//public class WebClientAccountClient implements AccountClient{
//
//    private final WebClient webClient;
//
//    public WebClientAccountClient(WebClient webClient) {
//        this.webClient = webClient;
//    }
//
//    @Override
//    public AccountResponse getAccountById(Long accountId) {
//        return webClient.get()
//                .uri("/api/accounts/{accountId}", accountId)
//                .retrieve()
//                .bodyToMono(AccountResponse.class)
//                .block();
//    }
//
//    @Override
//    public AccountResponse debitAccount(Long accountId, BigDecimal amount) {
//        return null;
//    }
//
//    @Override
//    public AccountResponse creditAccount(Long accountId, BigDecimal amount) {
//        return null;
//    }
//}
