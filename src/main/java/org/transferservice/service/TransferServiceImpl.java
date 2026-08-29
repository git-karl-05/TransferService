package org.transferservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.transferservice.client.AccountClient;
import org.transferservice.client.dto.AccountResponse;
import org.transferservice.client.dto.AccountStatus;
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

    public TransferServiceImpl(TransferRepository transferRepository, AccountClient accountClient) {
        this.transferRepository = transferRepository;
        this.accountClient = accountClient;
    }

    @Override
    public TransferResponse createTransfer(TransferRequest request) {

        if (request == null) {
            throw new InvalidTransferRequestException("Transfer request cannot be null");
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

        AccountResponse sourceAccount = accountClient.getAccountById(request.getFromAccountId());
        AccountResponse destinationAccount = accountClient.getAccountById(request.getToAccountId());

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


        log.info("Creating transfer from source account {} to destination account {}", request.getFromAccountId(), request.getToAccountId());
        TransferEntity entity = new TransferEntity();

        entity.setFromAccountId(request.getFromAccountId());
        entity.setToAccountId(request.getToAccountId());
        entity.setAmount(request.getAmount());
        entity.setDescription(request.getDescription());
        entity.setStatus(TransferStatus.COMPLETED);

        log.info("Saving transfer request");
        TransferEntity savedTransfer = transferRepository.save(entity);
        return new TransferResponse(savedTransfer);
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

    private TransferEntity getTransferEntity(Long id) {
        TransferEntity existingEntity = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException(id));

        return existingEntity;
    }
}
