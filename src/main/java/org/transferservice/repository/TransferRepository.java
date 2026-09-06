package org.transferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transferservice.entity.TransferEntity;

import java.util.Optional;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {

    Optional<TransferEntity> findByIdempotencyKey(String idempotencyKey);
}
