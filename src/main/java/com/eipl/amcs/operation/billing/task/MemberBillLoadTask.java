package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class MemberBillLoadTask extends Task<List<MemberBill>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberBillLoadTask.class);
    private final SocietyPaymentCycle paymentCycle;
    private final short generate;
    private final Society society;
    private final LocalDate deductionFromDate;
    private final LocalDate deductionToDate;

    public MemberBillLoadTask(SocietyPaymentCycle paymentCycle, Society society, LocalDate fromDate, LocalDate toDate, short generate) {
        this.paymentCycle = paymentCycle;
        this.generate = generate;
        this.deductionFromDate = fromDate;
        this.deductionToDate = toDate;
        this.society = society;
    }

    @Override
    protected List<MemberBill> call() throws Exception {
        try {
            SocietyPaymentCycleService paymentCycleService = EmcsAppContext.getContext().getBean(SocietyPaymentCycleService.class);

            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);
            List<MemberBill> memberListResult;
            SocietyPaymentCycle paymentCycle1 = null;

            if (generate == 0) {
                paymentCycle1 = paymentCycleService.findById(paymentCycle.getCode())
                        .orElseThrow(() -> new EntityNotFoundException(SocietyPaymentCycle.class, "invalid.paymentcycle"));
                memberListResult = service.fetchTableData(paymentCycle1);
            } else {
                paymentCycle1 = paymentCycleService.findById(paymentCycle.getCode())
                        .orElseThrow(() -> new EntityNotFoundException(SocietyPaymentCycle.class, "code", "invalid.paymentcycle"));
                SocietyPaymentCycle prevPaymentCycle = paymentCycleService.fetchCurrentPaymentCycle(paymentCycle1.getFromDate().minusDays(3), null);
                memberListResult = service.findMemberBill(society.getCode(), paymentCycle1, prevPaymentCycle, deductionFromDate, deductionToDate);
            }
            if (memberListResult == null || memberListResult.isEmpty()) return null;
            return memberListResult;
        } catch (Exception e) {
            LOGGER.error("Memberbill fetch", e);
            if (e.getMessage().contains("overlapping"))
                throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
