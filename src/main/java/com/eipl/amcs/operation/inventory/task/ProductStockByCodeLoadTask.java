package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductStock;
import com.eipl.amcs.operation.inventory.service.ProductStockService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductStockByCodeLoadTask extends Task<ProductStock> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStockByCodeLoadTask.class);

    private final String code;

    public ProductStockByCodeLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected ProductStock call() throws Exception {
        try {
            ProductStockService service = EmcsAppContext.getContext().getBean(ProductStockService.class);
            return service.findByProduct(code);
        } catch (Exception e) {
            LOGGER.error("ProductStockById fetch", e);
        }
        return null;
    }
}
