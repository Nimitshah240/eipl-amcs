package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CouponBalanceRepository extends BaseRepository<CouponBalance, String> {

    CouponBalance findFirstByConsumerTypeAndConsumerCodeAndMilkType(
            int consumerType,
            String consumerCode,
            MilkType milkType
    );

    @EntityGraph(attributePaths = {"milkType", "society", "union"})
    CouponBalance findByCouponBalanceCode(String couponBalanceCode);

    @Override
    @EntityGraph(attributePaths = {"milkType", "society", "union"})
    List<CouponBalance> findAll(Sort sort);

    @Query("SELECT cb.consumerCode, SUM(cb.balance), MAX(cb.createdAt), mt.code, mt.name, MAX(cb.consumerType) FROM CouponBalance cb JOIN cb.milkType mt GROUP BY cb.consumerCode, mt")
    List<Object[]> findAllGroupedByConsumerCode();
}
