package org.transferservice.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.transferservice.dto.TransferRequest;
import org.transferservice.dto.TransferResponse;
import org.transferservice.repository.TransferRepository;

import java.util.List;

public interface TransferService {

    TransferResponse createTransfer(TransferRequest request);

    TransferResponse getTransferById(Long transferId);

    List<TransferResponse> getAllTransfers();
}
