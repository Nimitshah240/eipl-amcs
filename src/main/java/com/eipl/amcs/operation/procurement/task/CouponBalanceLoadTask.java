package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.service.CouponBalanceService;
import com.eipl.amcs.operation.procurement.service.CouponIssueService;
import javafx.concurrent.Task;

import java.util.List;

public class CouponBalanceLoadTask extends Task<List<CouponBalance>> {

    @Override
    protected List<CouponBalance> call() throws Exception {
        CouponBalanceService service = EmcsAppContext.getContext().getBean(CouponBalanceService.class);
        return service.fetchAllGroupedByConsumerCode();
    }

}