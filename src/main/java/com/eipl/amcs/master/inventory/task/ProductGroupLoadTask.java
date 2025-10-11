package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.service.ProductGroupService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductGroupLoadTask extends Task<List<ProductGroup>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductGroupLoadTask.class);
    private ProductGroupService service;

    @Override
    protected List<ProductGroup> call() throws Exception {
        try {
            List<ProductGroup> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductGroup fetch", e);
        }
        return null;
    }
}
