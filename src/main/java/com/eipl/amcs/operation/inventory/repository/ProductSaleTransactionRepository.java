package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSaleTransactionRepository extends BaseRepository<ProductSaleTransaction, String> {

    @Override
    @EntityGraph(attributePaths = {"productSale", "product"})
    Optional<ProductSaleTransaction> findById(String id);

    @Override
    @EntityGraph(attributePaths = {"productSale", "product"})
    List<ProductSaleTransaction> findAll(Sort sort);

    @EntityGraph(attributePaths = {"productSale", "product"})
    List<ProductSaleTransaction> findByProductSale(ProductSale productSale);

}
