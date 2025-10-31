package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductStock;
import com.eipl.amcs.operation.inventory.service.ProductStockService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductStockLoadTask extends Task<List<ProductStock>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStockLoadTask.class);

    @Override
    protected List<ProductStock> call() throws Exception {
        try {
            ProductStockService service = EmcsAppContext.getContext().getBean(ProductStockService.class);
            List<ProductStock> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductStock fetch", e);
        }
        return null;
    }
}
