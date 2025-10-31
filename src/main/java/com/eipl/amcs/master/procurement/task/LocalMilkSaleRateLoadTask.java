package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.service.LocalMilkSaleRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LocalMilkSaleRateLoadTask extends Task<List<LocalMilkSaleRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleRateLoadTask.class);

    @Override
    protected List<LocalMilkSaleRate> call() throws Exception {
        try {
            LocalMilkSaleRateService service = EmcsAppContext.getContext().getBean(LocalMilkSaleRateService.class);
            List<LocalMilkSaleRate> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("LocalMilkSaleRates fetch", e);
        }
        return null;
    }
}

