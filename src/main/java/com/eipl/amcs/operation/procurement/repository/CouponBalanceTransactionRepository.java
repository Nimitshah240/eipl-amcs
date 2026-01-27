package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponBalanceTransaction;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponBalanceTransactionRepository extends BaseRepository<CouponBalanceTransaction, String> {

    CouponBalanceTransaction findTopByConsumerTypeAndConsumerCodeAndMilkTypeAndMilkClassOrderByTransactionDateDescCreatedAtDesc(
            int consumerType,
            String consumerCode,
            MilkType milkType,
            MilkClass milkClass
    );
}
