package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;

import java.math.BigDecimal;

public class MemberTotalAmountLoadTask extends Task<BigDecimal> {
    private final String societyPaymentCycleCode;
    private final String memberCode;

    public MemberTotalAmountLoadTask(String societyPaymentCycleCode, String memberCode) {
        this.societyPaymentCycleCode = societyPaymentCycleCode;
        this.memberCode = memberCode;
    }

    @Override
    protected BigDecimal call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            BigDecimal response = service.findTotalAmount(societyPaymentCycleCode, memberCode);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
