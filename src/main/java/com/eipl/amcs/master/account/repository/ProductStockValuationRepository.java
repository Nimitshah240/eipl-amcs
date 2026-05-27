package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.ProductStockValuation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductStockValuationRepository extends BaseRepository<ProductStockValuation, String> {
    @Query(value = "SELECT * " +
            "FROM tbl_product_stock_valuation " +
            "WHERE generated_at = ( " +
            "SELECT MAX(generated_at) " +
            "FROM tbl_product_stock_valuation " +
            "WHERE generated_at <=:targetDate " +
            ") ORDER BY product_code ", nativeQuery = true)
    List<ProductStockValuation> findAllByNearestDate(@Param("targetDate") LocalDate targetDate);

    @Modifying
    @Transactional
    void deleteByGeneratedAt(@Param("targetDate") LocalDate targetDate);

}