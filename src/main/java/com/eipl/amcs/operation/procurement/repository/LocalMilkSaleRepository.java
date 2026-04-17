package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocalMilkSaleRepository extends BaseRepository<LocalMilkSale, String> {

    @Override
    @EntityGraph(attributePaths = {"shift", "milkType", "milkClass", "society", "dock"})
    List<LocalMilkSale> findAll(Sort sort);

    @EntityGraph(attributePaths = {"shift", "milkType", "milkClass", "society", "dock"})
    List<LocalMilkSale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate, Sort sort);

    @EntityGraph(attributePaths = {"shift", "milkType", "milkClass", "society", "dock"})
    List<LocalMilkSale> findBySaleDateBetweenAndPaymentMode(LocalDateTime startDate, LocalDateTime endDate, Short paymentMode, Sort sort);

    @Override
    @EntityGraph(attributePaths = {"shift", "milkType", "milkClass", "society", "dock"})
    Optional<LocalMilkSale> findById(String id);

    @Query(value = "SELECT COALESCE(SUM(l.amount), 0) FROM LocalMilkSale l WHERE l.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal findLocalSaleAmountBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
