package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.master.global.model.Shift;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    List<LocalMilkSale> findByConsumerCodeAndConsumerTypeAndPaymentModeAndSaleDateBetween(
            String consumerCode,
            short consumerType,
            short paymentMode,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("SELECT COUNT(l) FROM LocalMilkSale l WHERE l.society = :society " +
           "AND l.saleDate >= :startDateTime AND l.saleDate <= :endDateTime " +
           "AND l.shift.id = :shiftCode")
    long countSalesByShiftDetails(
            @Param("society") Society society,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("shiftCode") Integer shiftCode);
}
