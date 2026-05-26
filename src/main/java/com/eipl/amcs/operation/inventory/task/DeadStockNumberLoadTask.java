package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadStockNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadStockNumberLoadTask.class);
    private final String societyCode;

    public DeadStockNumberLoadTask(String societyCode) {
        this.societyCode = societyCode;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService service = EmcsAppContext.getContext().getBean(NextCodeService.class);
            return service.getNextCode("DeadStock", "code", societyCode, 0);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch next dead stock number", e);
        }
        return null;
    }
}
