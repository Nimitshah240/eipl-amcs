package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.service.ProductSaleService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ProductSaleLoadTask extends Task<List<ProductSale>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptLoadTask.class);

    private final LocalDate fromDate;
    private final LocalDate toDate;

    public ProductSaleLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<ProductSale> call() throws Exception {
        try {
            ProductSaleService service = EmcsAppContext.getContext().getBean(ProductSaleService.class);
            List<ProductSale> list = service.findAll(fromDate, toDate);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductSale fetch", e);
        }
        return null;
    }
}
