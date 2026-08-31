package org.transferservice;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.RestController;
import org.transferservice.client.AccountClient;
import org.transferservice.client.FraudClient;
import org.transferservice.client.dto.*;
import org.transferservice.dto.TransferRequest;
import org.transferservice.dto.TransferResponse;
import org.transferservice.entity.TransferEntity;
import org.transferservice.entity.TransferStatus;
import org.transferservice.exception.InvalidTransferRequestException;
import org.transferservice.repository.TransferRepository;
import org.transferservice.service.TransferService;
import org.transferservice.service.TransferServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountClient accountClient;

    @Mock
    private FraudClient fraudClient;

    @InjectMocks
    private TransferServiceImpl transferService;



    @Test
    void createTransfer_shouldCompleteTransferSuccessfully() {

        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1L);
        request.setToAccountId(2L);
        request.setAmount(BigDecimal.valueOf(500));
        request.setDescription("Test transfer");


        AccountResponse sourceAccount = new AccountResponse();
        sourceAccount.setAccountId(1L);
        sourceAccount.setCustomerId(1L);
        sourceAccount.setAccountNumber("1001");
        sourceAccount.setAccountType(AccountType.CHECKINGS);
        sourceAccount.setBalance(BigDecimal.valueOf(2000));
        sourceAccount.setAccountStatus(AccountStatus.ACTIVE);
        sourceAccount.setCreatedAt(LocalDateTime.now());

        AccountResponse destinationAccount = new AccountResponse();
        destinationAccount.setAccountId(2L);
        destinationAccount.setCustomerId(2L);
        destinationAccount.setAccountNumber("2002");
        destinationAccount.setAccountType(AccountType.CHECKINGS);
        destinationAccount.setBalance(BigDecimal.valueOf(1000));
        destinationAccount.setAccountStatus(AccountStatus.ACTIVE);
        destinationAccount.setCreatedAt(LocalDateTime.now());

        FraudCheckResponse response = new FraudCheckResponse();
        response.setFraudCheckId(100L);
        response.setDecision(FraudDecision.APPROVED);
        response.setRiskLevel(FraudRiskLevel.LOW);
        response.setReason("Approved");

        AccountResponse debitedAccount = new AccountResponse();
        debitedAccount.setAccountId(1L);
        debitedAccount.setBalance(BigDecimal.valueOf(1500));
        debitedAccount.setAccountStatus(AccountStatus.ACTIVE);

        AccountResponse creditedAccount = new AccountResponse();
        creditedAccount.setAccountId(2L);
        creditedAccount.setBalance(BigDecimal.valueOf(1500));
        creditedAccount.setAccountStatus(AccountStatus.ACTIVE);

        TransferEntity savedEntity = new TransferEntity();
        savedEntity.setTransferId(10L);
        savedEntity.setFromAccountId(1L);
        savedEntity.setToAccountId(2L);
        savedEntity.setAmount(BigDecimal.valueOf(500));
        savedEntity.setDescription("Test transfer");
        savedEntity.setStatus(TransferStatus.COMPLETED);

        when(accountClient.getAccountById(1L))
                .thenReturn(sourceAccount);

        when(accountClient.getAccountById(2L))
                .thenReturn(destinationAccount);

        when(fraudClient.evaluateTransfer(
                1L,
                2L,
                BigDecimal.valueOf(500)))
                .thenReturn(response);

        when(accountClient.debitAccount(
                1L,
                BigDecimal.valueOf(500)))
                .thenReturn(debitedAccount);

        when(accountClient.creditAccount(
                2L,
                BigDecimal.valueOf(500)))
                .thenReturn(creditedAccount);

        when(transferRepository.save(any(TransferEntity.class)))
                .thenReturn(savedEntity);

        TransferResponse result = transferService.createTransfer(request);




        assertEquals(BigDecimal.valueOf(2000), result.getSourceStartingBalance());
        assertEquals(BigDecimal.valueOf(1500), result.getSourceEndingBalance());

        assertEquals(BigDecimal.valueOf(1000), result.getDestinationStartingBalance());
        assertEquals(BigDecimal.valueOf(1500), result.getDestinationEndingBalance());
    }

    @Test
    public void createTransfer_onNonActiveAccount_shouldThrow_InvalidTransferRequestException() {

        TransferRequest request = createTransferRequest();

        AccountResponse sourceAccount = createSourceAccount();

        AccountResponse destinationAccount = createDestinationAccount();
        destinationAccount.setAccountStatus(AccountStatus.FROZEN);


        when(accountClient.getAccountById(1L))
                .thenReturn(sourceAccount);

        when(accountClient.getAccountById(2L))
                .thenReturn(destinationAccount);


        assertThrows(InvalidTransferRequestException.class, () -> transferService.createTransfer(request));

        verify(fraudClient, never())
                .evaluateTransfer(anyLong(), anyLong(), any());

        verify(accountClient, never())
                .debitAccount(anyLong(), any());

        verify(accountClient, never())
                .creditAccount(anyLong(), any());

        verify(transferRepository, never())
                .save(any());
    }

    @Test
    public void fraudClientEvaluateTransfer_returnsRejected_shouldThrow_InvalidTransferRequestException() {

        TransferRequest request = createTransferRequest();

        AccountResponse sourceAccount = createSourceAccount();

        AccountResponse destinationAccount = createDestinationAccount();

        FraudCheckResponse rejectedResponse = createFraudCheckResponse();
        rejectedResponse.setDecision(FraudDecision.REJECTED);
        rejectedResponse.setRiskLevel(FraudRiskLevel.HIGH);
        rejectedResponse.setReason("Rejected");

        when(accountClient.getAccountById(1L))
                .thenReturn(sourceAccount);

        when(accountClient.getAccountById(2L))
                .thenReturn(destinationAccount);

        when(
                fraudClient.evaluateTransfer(
                1L,
                2L,
                BigDecimal.valueOf(500)
        ))
                .thenReturn(rejectedResponse);

        assertThrows(InvalidTransferRequestException.class, () -> transferService.createTransfer(request));

        verify(accountClient, never())
                .debitAccount(anyLong(), any());

        verify(accountClient, never())
                .creditAccount(anyLong(), any());

        verify(transferRepository, never())
                .save(any());


    }


    @Test
    public void createTransfer_withInsufficientFunds_shouldThrow_invalidTransferRequestException() {

        TransferRequest request = createTransferRequest();

        AccountResponse sourceAccount = createSourceAccount();
        sourceAccount.setBalance(BigDecimal.ZERO);

        AccountResponse destinationAccount = createDestinationAccount();


        when(accountClient.getAccountById(1L))
                .thenReturn(sourceAccount);

        when(accountClient.getAccountById(2L))
                .thenReturn(destinationAccount);

        assertThrows(InvalidTransferRequestException.class, () -> transferService.createTransfer(request));

        verify(fraudClient, never())
                .evaluateTransfer(anyLong(), anyLong(), any());

        verify(accountClient, never())
                .debitAccount(anyLong(), any());

        verify(accountClient, never())
                .creditAccount(anyLong(), any());

        verify(transferRepository, never())
                .save(any());
    }

    private TransferRequest createTransferRequest() {

        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1L);
        request.setToAccountId(2L);
        request.setAmount(BigDecimal.valueOf(500));
        request.setDescription("Test transfer");

        return request;
    }
    private AccountResponse createSourceAccount() {

        AccountResponse sourceAccount = new AccountResponse();
        sourceAccount.setAccountId(1L);
        sourceAccount.setCustomerId(1L);
        sourceAccount.setAccountNumber("1001");
        sourceAccount.setAccountType(AccountType.CHECKINGS);
        sourceAccount.setBalance(BigDecimal.valueOf(2000));
        sourceAccount.setAccountStatus(AccountStatus.ACTIVE);
        sourceAccount.setCreatedAt(LocalDateTime.now());

        return  sourceAccount;
    }
    private AccountResponse createDestinationAccount() {

        AccountResponse destinationAccount = new AccountResponse();
        destinationAccount.setAccountId(2L);
        destinationAccount.setCustomerId(2L);
        destinationAccount.setAccountNumber("2002");
        destinationAccount.setAccountType(AccountType.CHECKINGS);
        destinationAccount.setBalance(BigDecimal.valueOf(1000));
        destinationAccount.setAccountStatus(AccountStatus.ACTIVE);
        destinationAccount.setCreatedAt(LocalDateTime.now());

        return destinationAccount;
    }
    private FraudCheckResponse createFraudCheckResponse() {

        FraudCheckResponse response = new FraudCheckResponse();
        response.setFraudCheckId(100L);
        response.setDecision(FraudDecision.APPROVED);
        response.setRiskLevel(FraudRiskLevel.LOW);
        response.setReason("Approved");

        return response;
    }
    private AccountResponse createDebitedAccount() {

        AccountResponse debitedAccount = new AccountResponse();
        debitedAccount.setAccountId(1L);
        debitedAccount.setBalance(BigDecimal.valueOf(1500));
        debitedAccount.setAccountStatus(AccountStatus.ACTIVE);

        return debitedAccount;
    }
    private AccountResponse createCreditedAccount() {

        AccountResponse creditedAccount = new AccountResponse();
        creditedAccount.setAccountId(2L);
        creditedAccount.setBalance(BigDecimal.valueOf(1500));
        creditedAccount.setAccountStatus(AccountStatus.ACTIVE);

        return creditedAccount;
    }
    private TransferEntity createSavedEntity() {

        TransferEntity entity = new TransferEntity();
        entity.setTransferId(10L);
        entity.setFromAccountId(1L);
        entity.setToAccountId(2L);
        entity.setAmount(BigDecimal.valueOf(500));
        entity.setDescription("Test transfer");
        entity.setStatus(TransferStatus.COMPLETED);

        return entity;
    }

}
