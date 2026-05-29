package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReceiptRepository extends BaseRepository<ProductReceipt, String> {

    @Override
    @EntityGraph(attributePaths = {"customer", "union", "society", "customer.ledger"})
    Optional<ProductReceipt> findById(String id);

    @Override
    @EntityGraph(attributePaths = {"customer", "union", "society", "customer.ledger"})
    List<ProductReceipt> findAll(Sort sort);

    @EntityGraph(attributePaths = {"customer", "union", "society", "customer.ledger"})
    List<ProductReceipt> findByGrnDateBetween(LocalDate fromDate, LocalDate toDate, Sort sort);
}
