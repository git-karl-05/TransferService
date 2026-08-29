package org.transferservice.client;

import org.transferservice.client.dto.AccountResponse;

public interface AccountClient {


    AccountResponse getAccountById(Long accountId);
}
