package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.service.ProductPurchaseRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductPurchaseRateLoadTask extends Task<List<ProductPurchaseRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPurchaseRateLoadTask.class);

    @Override
    protected List<ProductPurchaseRate> call() throws Exception {
        try {
            ProductPurchaseRateService service = EmcsAppContext.getContext().getBean(ProductPurchaseRateService.class);
            List<ProductPurchaseRate> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductPurchaseRate fetch", e);
        }
        return null;
    }
}
