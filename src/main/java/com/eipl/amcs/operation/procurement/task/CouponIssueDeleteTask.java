package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.service.CouponIssueService;
import javafx.concurrent.Task;

public class CouponIssueDeleteTask extends Task<Boolean> {
    private final CouponIssue couponIssue;
    private final int source;
    private final String orgType;
    private final String operation;

    public CouponIssueDeleteTask(CouponIssue couponIssue, int source, String orgType, String operation) {
        this.couponIssue = couponIssue;
        this.source = source;
        this.orgType = orgType;
        this.operation = operation;
    }

    @Override
    protected Boolean call() throws Exception {
        CouponIssueService service = EmcsAppContext.getContext().getBean(CouponIssueService.class);

        couponIssue.setIsDelete(true);
        return service.delete(couponIssue, source, orgType, operation);
    }
}
