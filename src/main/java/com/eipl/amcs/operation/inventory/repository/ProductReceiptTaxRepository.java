package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTax;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReceiptTaxRepository extends BaseRepository<ProductReceiptTax, String> {

    @Override
    @EntityGraph(attributePaths = {"productReceipt", "productReceiptTransaction", "taxDetail"})
    Optional<ProductReceiptTax> findById(String id);

    @Override
    @EntityGraph(attributePaths = {"productReceipt", "productReceiptTransaction", "taxDetail"})
    List<ProductReceiptTax> findAll(Sort sort);

    @EntityGraph(attributePaths = {"productReceipt", "productReceiptTransaction", "taxDetail"})
    List<ProductReceiptTax> findByproductReceiptTransaction(ProductReceiptTransaction productReceiptTransaction);

    @EntityGraph(attributePaths = {"productReceipt", "productReceiptTransaction", "taxDetail"})
    List<ProductReceiptTax> findByProductReceipt(ProductReceipt productReceipt);

}
