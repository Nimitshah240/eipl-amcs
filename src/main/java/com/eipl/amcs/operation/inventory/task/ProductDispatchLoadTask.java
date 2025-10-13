package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import com.eipl.amcs.operation.inventory.service.ProductDispatchService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ProductDispatchLoadTask extends Task<List<ProductDispatchTransaction>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDispatchLoadTask.class);

    private final LocalDate fromDate;
    private final LocalDate toDate;

    public ProductDispatchLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<ProductDispatchTransaction> call() throws Exception {
        try {
            ProductDispatchService service = EmcsAppContext.getContext().getBean(ProductDispatchService.class);
            List<ProductDispatchTransaction> list = service.findByDispatchDate(fromDate, toDate);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductDispatch fetch", e);
        }
        return null;
    }
}
