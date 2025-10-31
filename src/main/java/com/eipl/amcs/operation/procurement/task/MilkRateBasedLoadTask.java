package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import javafx.concurrent.Task;

import java.util.List;

public class MilkRateBasedLoadTask extends Task<List<MemberMilkPurchaseRateBased>> {
    private final String rateCode;

    public MilkRateBasedLoadTask(String rateCode) {
        this.rateCode = rateCode;
    }

    @Override
    protected List<MemberMilkPurchaseRateBased> call() throws Exception {
        try {

            MemberMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(MemberMilkPurchaseRateService.class);
            List<MemberMilkPurchaseRateBased> list = (service.fetchRateBased(rateCode));

            if (list == null || list.isEmpty()) return null;
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
