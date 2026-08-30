package org.transferservice.client;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.transferservice.client.dto.FraudCheckRequest;
import org.transferservice.client.dto.FraudCheckResponse;

import java.math.BigDecimal;

@Component
public class RestClientFraudClient implements FraudClient {

    private final RestClient restClient;

    public RestClientFraudClient(@Qualifier("fraudRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public FraudCheckResponse evaluateTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        FraudCheckRequest request = new FraudCheckRequest();
        request.setFromAccountId(fromAccountId);
        request.setToAccountId(toAccountId);
        request.setAmount(amount);

        return restClient.post()
                .uri("/api/fraud/check")
                .body(request)
                .retrieve()
                .body(FraudCheckResponse.class);
    }
}
