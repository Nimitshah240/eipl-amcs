package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import com.eipl.amcs.operation.procurement.dto.MilkRateAndDetailsDto;
import javafx.concurrent.Task;

public class MilkRateAndDetailsLoadTask extends Task<MilkRateAndDetailsDto> {
    private final String code;

    public MilkRateAndDetailsLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected MilkRateAndDetailsDto call() throws Exception {
        try {
            MemberMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(MemberMilkPurchaseRateService.class);
            return service.fetchRateAndDetails(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
