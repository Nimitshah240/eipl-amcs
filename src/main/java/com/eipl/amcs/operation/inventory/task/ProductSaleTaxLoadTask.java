package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductSaleTax;
import com.eipl.amcs.operation.inventory.service.ProductSaleTaxService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductSaleTaxLoadTask extends Task<List<ProductSaleTax>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleTaxLoadTask.class);

    @Override
    protected List<ProductSaleTax> call() throws Exception {
        try {
            ProductSaleTaxService service = EmcsAppContext.getContext().getBean(ProductSaleTaxService.class);
            List<ProductSaleTax> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductSaleToMemberTaxCalculated fetch", e);
        }
        return null;
    }
}
