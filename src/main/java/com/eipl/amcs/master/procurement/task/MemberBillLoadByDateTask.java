package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;

public class MemberBillLoadByDateTask extends Task<List<MemberBillSummary>> {

    private LocalDate fromDate, toDate;

    public MemberBillLoadByDateTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }


    @Override
    protected List<MemberBillSummary> call() throws Exception {
        try {
            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);
            return service.findMemberBillSummaryBetWeenFromDateAndToDate(fromDate, toDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

