package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.service.CouponIssueService;
import javafx.concurrent.Task;

public class CouponIssueSaveTask extends Task<Boolean> {
    private final CouponIssue couponIssue;
    private final short update;

    public CouponIssueSaveTask(CouponIssue couponIssue, short update) {
        this.couponIssue = couponIssue;
        this.update = update;
    }

    @Override
    protected Boolean call() throws Exception {

        if (couponIssue == null) {
            return false;
        }
        CouponIssueService service = EmcsAppContext.getContext().getBean(CouponIssueService.class);
        if (this.update == 0) {
            return service.insert(couponIssue);
        } else {
            return service.update(couponIssue);
        }
    }
}
