package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReceiptTransactionRepository extends BaseRepository<ProductReceiptTransaction, String> {

    @Override
    @EntityGraph(attributePaths = {"productReceipt", "product", "tax", "unit"})
    Optional<ProductReceiptTransaction> findById(String id);

    @Override
    @EntityGraph(attributePaths = {"productReceipt", "product", "tax", "unit"})
    List<ProductReceiptTransaction> findAll(Sort sort);

    @EntityGraph(attributePaths = {"productReceipt", "product", "tax", "unit"})
    List<ProductReceiptTransaction> findByProductReceipt(ProductReceipt productreceipt);
}
