package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.service.ProductSaleRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductSaleRateLoadTask extends Task<List<ProductSaleRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleRateLoadTask.class);

    @Override
    protected List<ProductSaleRate> call() throws Exception {
        try {
            ProductSaleRateService service = EmcsAppContext.getContext().getBean(ProductSaleRateService.class);
            List<ProductSaleRate> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductSaleRate fetch", e);
        }
        return null;
    }
}
