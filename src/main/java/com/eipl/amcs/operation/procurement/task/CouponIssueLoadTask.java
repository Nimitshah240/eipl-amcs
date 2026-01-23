package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.service.CouponIssueService;
import javafx.concurrent.Task;

import java.util.List;

public class CouponIssueLoadTask extends Task<List<CouponIssue>> {

    @Override
    protected List<CouponIssue> call() throws Exception {
        CouponIssueService service = EmcsAppContext.getContext().getBean(CouponIssueService.class);
        return service.fetchAll();
    }

}