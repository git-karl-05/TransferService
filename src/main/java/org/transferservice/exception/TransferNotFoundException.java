package org.transferservice.exception;

public class TransferNotFoundException extends RuntimeException{

    public TransferNotFoundException(Long transferId) {
        super("Transfer with Transfer ID: " + transferId + " not found");
    }
}
