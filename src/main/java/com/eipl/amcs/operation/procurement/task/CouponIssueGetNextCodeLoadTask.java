package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.service.CouponIssueService;
import javafx.concurrent.Task;

public class CouponIssueGetNextCodeLoadTask extends Task<String> {

    private Society society;

    public CouponIssueGetNextCodeLoadTask(Society society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        CouponIssueService service = EmcsAppContext.getContext().getBean(CouponIssueService.class);
        return service.getNextCode(society);
    }
}
