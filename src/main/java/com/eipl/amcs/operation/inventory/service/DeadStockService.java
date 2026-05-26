package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.model.DeadStock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DeadStockService {
    List<DeadStock> findAll();

    DeadStock save(DeadStock deadStock);

    DeadStock update(DeadStock deadStock);

    Optional<DeadStock> findById(String code);

    void delete(String code);

    DeadStock findByDeadStock(String code);

    void delete(DeadStock deadStock);

    List<DeadStock> findByPurchaseDateBetween(LocalDate fromDate, LocalDate toDate);
}
