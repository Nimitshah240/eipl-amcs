package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import javafx.concurrent.Task;

import java.util.List;

public class SocietyRateBasedLoadTask extends Task<List<SocietyMilkPurchaseRateBased>> {
    private final String code;

    public SocietyRateBasedLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<SocietyMilkPurchaseRateBased> call() throws Exception {
        try {

            SocietyMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);
            List<SocietyMilkPurchaseRateBased> list = (service.fetchRateBased(code));

            if (list == null || list.isEmpty()) return null;
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
