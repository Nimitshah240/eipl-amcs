package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductPurchaseRateNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPurchaseRateLoadTask.class);
    private final String productPurchaseRate;

    public ProductPurchaseRateNumberLoadTask(String productPurchaseRate) {
        this.productPurchaseRate = productPurchaseRate;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            return nextCodeService.getNextCode("ProductPurchaseRate", "code", MainApp.identityDto.getSociety().getCode(), 4);
        } catch (Exception e) {
            LOGGER.error("productPurchaseRate Number fetch", e);
        }
        return null;
    }
}
