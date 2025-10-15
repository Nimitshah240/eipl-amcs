package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.operation.inventory.service.ProductReceiptService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ProductReceiptLoadTask extends Task<List<ProductReceipt>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptLoadTask.class);

    private final LocalDate fromDate;
    private final LocalDate toDate;

    public ProductReceiptLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<ProductReceipt> call() throws Exception {
        try {

            ProductReceiptService service = EmcsAppContext.getContext().getBean(ProductReceiptService.class);
            List<ProductReceipt> list = service.findAll(fromDate, toDate);
            if (list == null | list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
