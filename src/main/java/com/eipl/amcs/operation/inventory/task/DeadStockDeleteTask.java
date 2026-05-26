package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.service.DeadStockService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadStockDeleteTask extends Task<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadStockDeleteTask.class);
    private final String code;

    public DeadStockDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            DeadStockService service = EmcsAppContext.getContext().getBean(DeadStockService.class);
            service.delete(code);
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to delete dead stock", e);
            return false;
        }
    }
}
