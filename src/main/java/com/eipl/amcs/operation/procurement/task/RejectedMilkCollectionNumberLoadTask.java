package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RejectedMilkCollectionNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectedMilkCollectionNumberLoadTask.class);
    private final String societyCode;

    public RejectedMilkCollectionNumberLoadTask(String societyCode) {
        this.societyCode = societyCode;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService service = EmcsAppContext.getContext().getBean(NextCodeService.class);
            return service.getNextCode("RejectedMilkCollection", "milkCollectionRejectedCode", societyCode, 0);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch next rejected milk collection number", e);
        }
        return null;
    }
}
