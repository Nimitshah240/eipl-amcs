package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberMilkPurchaseRateLoadTask extends Task<List<MemberMilkPurchaseRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberMilkPurchaseRateLoadTask.class);

    @Override
    protected List<MemberMilkPurchaseRate> call() throws Exception {
        try {
            MemberMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(MemberMilkPurchaseRateService.class);
            List<MemberMilkPurchaseRate> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("MemberMilkPurchaseRates fetch", e);
        }
        return null;
    }
}

