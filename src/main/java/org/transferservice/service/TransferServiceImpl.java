package org.transferservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.transferservice.client.AccountClient;
import org.transferservice.client.FraudClient;
import org.transferservice.client.dto.*;
import org.transferservice.dto.TransferRequest;
import org.transferservice.dto.TransferResponse;
import org.transferservice.entity.TransferEntity;
import org.transferservice.entity.TransferStatus;
import org.transferservice.exception.InvalidTransferRequestException;
import org.transferservice.exception.TransferNotFoundException;
import org.transferservice.repository.TransferRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Service
public class TransferServiceImpl implements TransferService{

    private static final BigDecimal TRANSFER_LIMIT = BigDecimal.valueOf(10000);
    private static final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);

    private final TransferRepository transferRepository;
    private final AccountClient accountClient;
    private final FraudClient fraudClient;

    public TransferServiceImpl(TransferRepository transferRepository, AccountClient accountClient, FraudClient fraudClient) {
        this.transferRepository = transferRepository;
        this.accountClient = accountClient;
        this.fraudClient = fraudClient;
    }


    @Override
    public TransferResponse createTransfer(TransferRequest request) {

        validateRequest(request);

        AccountResponse sourceAccount = accountClient.getAccountById(request.getFromAccountId());
        AccountResponse destinationAccount = accountClient.getAccountById(request.getToAccountId());

        validateAccount(sourceAccount, destinationAccount, request);

        FraudCheckResponse response = fraudClient.evaluateTransfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());

        log.info(
                "Fraud check completed with decision {} and risk level {}",
                response.getDecision(),
                response.getRiskLevel()
        );

        if (Objects.equals(response.getDecision(), FraudDecision.REJECTED)) {
            throw new InvalidTransferRequestException("Transfer denied due to fraud risk");
        }

        TransferEntity entity = new TransferEntity();

        entity.setFromAccountId(request.getFromAccountId());
        entity.setToAccountId(request.getToAccountId());
        entity.setAmount(request.getAmount());
        entity.setDescription(request.getDescription());
        entity.setStatus(TransferStatus.PENDING);

        TransferEntity pendingTransfer = transferRepository.save(entity);

        String debitOperationId = "TRANSFER-" + pendingTransfer.getTransferId() +"-DEBIT";
        String creditOperationId = "TRANSFER-" + pendingTransfer.getTransferId() +"-CREDIT";
        String compensationOperationId = "TRANSFER-" + pendingTransfer.getTransferId() +"-COMPENSATION";

        AccountResponse debitedAccount;
        AccountResponse creditedAccount;

        try {

            log.info("Processing debit amount: {} from source account: {}", request.getAmount(), request.getFromAccountId());
            debitedAccount = accountClient.debitAccount(request.getFromAccountId(), debitOperationId, request.getAmount());

        } catch (RuntimeException exception) {

            pendingTransfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(pendingTransfer);
            log.warn("Processing debit amount failed");

            throw exception;

        }

        try {

            log.info("Processing credit amount: {} to destination account: {}", request.getAmount(), request.getToAccountId());
            creditedAccount = accountClient.creditAccount(request.getToAccountId(), creditOperationId, request.getAmount());

        } catch (RuntimeException exception) {

            try {

                log.warn("Credit failed. Compensating source account");
                accountClient.creditAccount(request.getFromAccountId(), compensationOperationId, request.getAmount());

                pendingTransfer.setStatus(TransferStatus.COMPENSATED);
                transferRepository.save(pendingTransfer);

            } catch (RuntimeException compensationException) {

                pendingTransfer.setStatus(TransferStatus.COMPENSATION_FAILED);
                transferRepository.save(pendingTransfer);

                throw compensationException;
            }

            throw exception;

        }

        log.info("Transfer has been completed successfully");

        pendingTransfer.setStatus(TransferStatus.COMPLETED);
        TransferEntity completedTransfer = transferRepository.save(pendingTransfer);

        return new TransferResponse(
                completedTransfer,
                sourceAccount.getBalance(),
                debitedAccount.getBalance(),
                destinationAccount.getBalance(),
                creditedAccount.getBalance()
        );
    }

    @Override
    public TransferResponse getTransferById(Long transferId) {
        TransferEntity entity = getTransferEntity(transferId);

        return new TransferResponse(entity);
    }

    @Override
    public List<TransferResponse> getAllTransfers() {
        return transferRepository.findAll()
                .stream()
                .map(TransferResponse::new)
                .toList();

    }

    private void validateRequest(TransferRequest request) {
        if (request == null) {
            throw new InvalidTransferRequestException("Transfer request cannot be null");
        }

        if (request.getFromAccountId() == null || request.getToAccountId() == null) {
            throw new InvalidTransferRequestException("Source and destination account IDs are required");
        }

        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferRequestException("Transfer amount must be greater than zero");
        }

        if (Objects.equals(request.getFromAccountId(), request.getToAccountId())) {
            throw new InvalidTransferRequestException("Source and Destination accounts must be different");
        }

        if (request.getAmount().compareTo(TRANSFER_LIMIT) > 0) {
            throw new InvalidTransferRequestException("Transfer amount cannot exceed $10,000");
        }
    }

    private void validateAccount(AccountResponse sourceAccount, AccountResponse destinationAccount, TransferRequest request) {
        if (!AccountStatus.ACTIVE.equals(sourceAccount.getAccountStatus())) {
            throw new InvalidTransferRequestException("Source account must be active");
        }

        if (!AccountStatus.ACTIVE.equals(destinationAccount.getAccountStatus())) {
            throw new InvalidTransferRequestException("Destination account must be active");
        }

        if (sourceAccount.getBalance()
                .compareTo(request.getAmount()) < 0) {
            throw new InvalidTransferRequestException("Source account has insufficient funds");
        }
    }

    private TransferEntity getTransferEntity(Long id) {
        TransferEntity existingEntity = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException(id));

        return existingEntity;
    }
}
