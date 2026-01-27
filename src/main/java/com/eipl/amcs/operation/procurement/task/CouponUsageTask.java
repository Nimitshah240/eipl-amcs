package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.service.CouponIssueService;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import javafx.concurrent.Task;

public class CouponUsageTask extends Task<Double[]> {
    private final CouponIssue issue;
    public CouponUsageTask(CouponIssue issue) { this.issue = issue; }

    @Override
    protected Double[]  call() throws Exception {
        CouponIssueService couponIssueService = EmcsAppContext.getContext().getBean(CouponIssueService.class);
        LocalMilkSaleService localMilkSaleService = EmcsAppContext.getContext().getBean(LocalMilkSaleService.class);

        double countExceptCurrent = couponIssueService.fetchAllByMemberExceptCurrent(issue);
        double usedCount = localMilkSaleService.countCoupon(issue);

        return new Double[]{countExceptCurrent, usedCount};
    }
}
