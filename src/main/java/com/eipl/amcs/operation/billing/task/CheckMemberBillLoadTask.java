package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckMemberBillLoadTask extends Task<MemberBillSummary> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckMemberBillLoadTask.class);
    private final SocietyPaymentCycle paymentCycle;

    public CheckMemberBillLoadTask(SocietyPaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    @Override
    protected MemberBillSummary call() throws Exception {
        try {
            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);
            SocietyPaymentCycleService paymentCycleService = EmcsAppContext.getContext().getBean(SocietyPaymentCycleService.class);
            SocietyPaymentCycle paymentCycle1 = paymentCycleService.findById(paymentCycle.getCode())
                    .orElse(null);
            return service.checkTableData(paymentCycle1);
        } catch (Exception e) {
            LOGGER.error("Memberbill fetch", e);
        }
        return null;
    }
}
