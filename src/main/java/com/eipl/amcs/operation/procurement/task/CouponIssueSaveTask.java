package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.service.CouponIssueService;
import javafx.concurrent.Task;

public class CouponIssueSaveTask extends Task<Boolean> {
    private final CouponIssue couponIssue;
    private final int source;
    private final boolean isUpdate;
    private final String orgType;
    private final String operation;

    public CouponIssueSaveTask(CouponIssue couponIssue, boolean isUpdate, int source, String orgType, String operation) {
        this.couponIssue = couponIssue;
        this.isUpdate = isUpdate;
        this.source = source;
        this.orgType = orgType;
        this.operation = operation;
    }

    @Override
    protected Boolean  call() throws Exception {

        if (couponIssue == null) {
            return false;
        }
        CouponIssueService service = EmcsAppContext.getContext().getBean(CouponIssueService.class);
        if (isUpdate) {
            return service.insert(couponIssue, source, orgType, operation);
        }else{
            return service.update(couponIssue, source, orgType, operation);
        }

    }
}
