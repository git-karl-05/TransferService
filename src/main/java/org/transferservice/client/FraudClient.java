package org.transferservice.client;

import org.transferservice.client.dto.FraudCheckResponse;

import java.math.BigDecimal;

public interface FraudClient {

    FraudCheckResponse evaluateTransfer(
            Long fromAccountId,
            Long toAccountId,
            BigDecimal amount
    );
}
