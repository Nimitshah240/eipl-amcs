package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.operation.inventory.model.ProductStock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStockRepository extends BaseRepository<ProductStock, String> {

    @Override
    @EntityGraph(attributePaths = {"society", "product"})
    Optional<ProductStock> findById(String id);

    @Override
    @EntityGraph(attributePaths = {"society", "product"})
    List<ProductStock> findAll(Sort sort);

    @EntityGraph(attributePaths = {"society", "product"})
    Optional<ProductStock> findByProduct(Product product);

    @EntityGraph(attributePaths = {"society", "product"})
    Optional<ProductStock> findFirstByProductAndStockGreaterThan(Product product, BigDecimal limit, Sort sort);

    @EntityGraph(attributePaths = {"society", "product"})
    Optional<ProductStock> findByProductAndBatchNo(Product product, String batchNo);
}
