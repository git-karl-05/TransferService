package org.transferservice.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.TransferRequest;
import org.transferservice.dto.TransferResponse;
import org.transferservice.service.TransferService;
import org.transferservice.service.TransferServiceImpl;


@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }


    @GetMapping("/{transferId}")
    public ResponseEntity<TransferResponse> getTransferByTransferId(@PathVariable Long transferId) {
        TransferResponse response = transferService.getTransferById(transferId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.createTransfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
