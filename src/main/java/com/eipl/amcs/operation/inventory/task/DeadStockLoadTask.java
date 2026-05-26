package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.DeadStock;
import com.eipl.amcs.operation.inventory.service.DeadStockService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DeadStockLoadTask extends Task<List<DeadStock>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadStockLoadTask.class);

    @Override
    protected List<DeadStock> call() throws Exception {
        try {
            DeadStockService service = EmcsAppContext.getContext().getBean(DeadStockService.class);
            return service.findAll();
        } catch (Exception e) {
            LOGGER.error("Failed to fetch dead stock", e);
        }
        return null;
    }
}
