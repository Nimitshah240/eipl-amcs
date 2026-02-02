package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.service.CouponBalanceService;
import javafx.concurrent.Task;

public class CouponBalanceForConsumerFetchTask extends Task<CouponBalance> {
    private int consumerType;
    private String consumerCode;
    private MilkType milkType;

    public CouponBalanceForConsumerFetchTask(int consumerType, String consumerCode, MilkType milkType) {
        this.consumerType = consumerType;
        this.consumerCode = consumerCode;
        this.milkType = milkType;
    }

    @Override
    protected CouponBalance call() throws Exception {
        CouponBalanceService couponBalanceService = EmcsAppContext.getContext().getBean(CouponBalanceService.class);
        CouponBalance bal = couponBalanceService.fetchBalanceForConsumer(consumerType, consumerCode, milkType);
        return bal;
    }
}