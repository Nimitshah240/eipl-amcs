package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.service.BonusService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BonusListLoadTask extends Task<BonusDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BonusListLoadTask.class);
    private final String code;

    public BonusListLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected BonusDto call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);
            return service.findBySummary(code);
        } catch (Exception e) {
            LOGGER.error("Bonus fetch", e);
        }
        return null;
    }
}
