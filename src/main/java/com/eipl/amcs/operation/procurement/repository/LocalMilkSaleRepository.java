package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Override
    @EntityGraph(attributePaths = {"shift", "milkType", "milkClass", "society", "dock"})
    Optional<LocalMilkSale> findById(String id);

    @Query("SELECT SUM(l.coupon) FROM LocalMilkSale l " +
            "WHERE l.isDelete = :isDelete " +
            "AND l.consumerCode = :code " +
            "AND (l.consumerType = :type1 OR l.consumerType = :type2) " +
            "AND l.milkType = :milkType " +
            "AND l.milkClass = :milkClass " +
            "AND l.saleDate BETWEEN :fromDate AND :toDate")
    Double sumCouponByMemberAndTypePairs(
            @Param("isDelete") boolean isDelete,
            @Param("code") String code,
            @Param("type1") Short type1,
            @Param("type2") Short type2,
            @Param("milkType") MilkType milkType,
            @Param("milkClass") MilkClass milkClass,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );



}
