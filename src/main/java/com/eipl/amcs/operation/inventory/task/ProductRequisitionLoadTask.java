package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ProductRequisitionLoadTask extends Task<List<ProductRequisition>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRequisitionLoadTask.class);

    private final LocalDate fromDate;
    private final LocalDate toDate;

    public ProductRequisitionLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<ProductRequisition> call() throws Exception {
        try {
            ProductRequisitionService service = EmcsAppContext.getContext().getBean(ProductRequisitionService.class);
            List<ProductRequisition> list = service.findByDate(fromDate, toDate);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductRequisition fetch", e);
        }
        return null;
    }
}
