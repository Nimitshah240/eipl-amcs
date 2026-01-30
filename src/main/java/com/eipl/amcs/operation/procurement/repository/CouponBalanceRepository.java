package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;


@Repository
public interface CouponBalanceRepository extends BaseRepository<CouponBalance, String> {

    CouponBalance findFirstByConsumerTypeAndConsumerCodeAndMilkType(
            int consumerType,
            String consumerCode,
            MilkType milkType
    );

    @EntityGraph(attributePaths = {"milkType", "milkClass", "society", "union"})
    CouponBalance findByCouponBalanceCode(String couponBalanceCode);
}
