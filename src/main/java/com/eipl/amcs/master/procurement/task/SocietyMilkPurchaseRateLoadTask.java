package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SocietyMilkPurchaseRateLoadTask extends Task<List<SocietyMilkPurchaseRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyMilkPurchaseRateLoadTask.class);

    @Override
    protected List<SocietyMilkPurchaseRate> call() throws Exception {
        try {
            SocietyMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);
            List<SocietyMilkPurchaseRate> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("SocietyMilkPurchaseRates fetch", e);
        }
        return null;
    }
}

