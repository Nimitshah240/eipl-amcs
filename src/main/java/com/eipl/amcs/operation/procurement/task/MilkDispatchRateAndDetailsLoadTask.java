package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;
import javafx.concurrent.Task;

public class MilkDispatchRateAndDetailsLoadTask extends Task<MilkDispatchRateAndDetailsDto> {
    private final String code;

    public MilkDispatchRateAndDetailsLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected MilkDispatchRateAndDetailsDto call() throws Exception {
        try {
            SocietyMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);
            return service.fetchRateAndDetails(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
