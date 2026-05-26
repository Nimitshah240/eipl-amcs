package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.DeadStock;
import com.eipl.amcs.operation.inventory.service.DeadStockService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadStockSaveTask extends Task<DeadStock> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadStockSaveTask.class);
    private final DeadStock deadStock;
    private final short saveOrUpdate;

    public DeadStockSaveTask(DeadStock deadStock, short saveOrUpdate) {
        this.deadStock = deadStock;
        this.saveOrUpdate = saveOrUpdate;
    }

    @Override
    protected DeadStock call() throws Exception {
        try {
            DeadStockService service = EmcsAppContext.getContext().getBean(DeadStockService.class);
            if (saveOrUpdate == 0) {
                return service.save(deadStock);
            } else {
                return service.update(deadStock);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save/update dead stock", e);
        }
        return null;
    }
}
