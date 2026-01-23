package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.service.CouponIssueService;
import javafx.concurrent.Task;

import java.time.LocalDate;

public class CouponIssueFetchLatestDateByMemberTask extends Task<LocalDate> {

    private final String consumerCode;
    private final int consumerType;
    private final CouponIssue currentIssue;


    public CouponIssueFetchLatestDateByMemberTask(String consumerCode, int consumerType, CouponIssue currentIssue) {
        this.consumerCode = consumerCode;
        this.consumerType = consumerType;
        this.currentIssue = currentIssue;
    }

    @Override
    protected LocalDate call() throws Exception {
        CouponIssueService service = EmcsAppContext.getContext().getBean(CouponIssueService.class);
        return service.fetchLatestDateByMember(consumerCode, consumerType, currentIssue);

    }
}
