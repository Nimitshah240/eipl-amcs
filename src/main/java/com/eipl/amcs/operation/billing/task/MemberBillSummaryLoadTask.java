package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberBillSummaryLoadTask extends Task<List<MemberBillSummary>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberBillSummaryLoadTask.class);

    @Override
    protected List<MemberBillSummary> call() throws Exception {
        try {
            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);
            List<MemberBillSummary> summaryList = service.findMemberBillSummaryBetWeen(MainApp.getFinancialYear().getStartDate(),
                    MainApp.getFinancialYear().getEndDate());
            if (summaryList == null || summaryList.isEmpty()) return null;
            return summaryList;
        } catch (Exception e) {
            LOGGER.error("Memberbill summary fetch", e);
        }
        return null;
    }
}
