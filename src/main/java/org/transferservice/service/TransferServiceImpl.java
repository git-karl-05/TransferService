package org.transferservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.transferservice.dto.TransferRequest;
import org.transferservice.dto.TransferResponse;
import org.transferservice.repository.TransferRepository;

import java.util.List;


@Service
public class DefaultTransferService implements TransferService{

    private final TransferRepository transferRepository;
    private final Logger log = LoggerFactory.getLogger(TransferService.class);

    public DefaultTransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public TransferResponse createTransfer(TransferRequest request) {
        return null;
    }

    @Override
    public TransferResponse getTransferById(Long transferId) {
        return null;
    }

    @Override
    public List<TransferResponse> getAllTransfers() {
        return List.of();
    }
}
