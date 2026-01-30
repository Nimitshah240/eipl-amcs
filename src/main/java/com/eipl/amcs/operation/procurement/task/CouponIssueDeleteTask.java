package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.service.CouponIssueService;
import javafx.concurrent.Task;

public class CouponIssueDeleteTask extends Task<Boolean> {
    private final CouponIssue couponIssue;

    public CouponIssueDeleteTask(CouponIssue couponIssue) {
        this.couponIssue = couponIssue;
    }

    @Override
    protected Boolean call() throws Exception {
        CouponIssueService service = EmcsAppContext.getContext().getBean(CouponIssueService.class);
        couponIssue.setIsDelete(true);
        return service.delete(couponIssue);
    }
}
