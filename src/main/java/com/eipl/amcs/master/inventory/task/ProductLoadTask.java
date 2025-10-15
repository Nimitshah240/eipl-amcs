package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.service.ProductGroupService;
import com.eipl.amcs.master.inventory.service.ProductService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductLoadTask extends Task<List<Product>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductLoadTask.class);

    @Override
    protected List<Product> call() throws Exception {
        try {
            ProductService service = EmcsAppContext.getContext().getBean(ProductService.class);
            List<Product> list = service.findAllBySociety(MainApp.identityDto.getSociety().getCode());

            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Product fetch", e);
        }
        return null;
    }
}
