package org.transferservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transferservice.entity.TransferEntity;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
}
