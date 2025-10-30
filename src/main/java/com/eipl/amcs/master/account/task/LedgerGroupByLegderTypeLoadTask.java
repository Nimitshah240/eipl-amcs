package com.eipl.amcs.master.account.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.service.LedgerGroupService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LedgerGroupByLegderTypeLoadTask extends Task<List<LedgerGroup>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask.class);

    private final Integer code;

    public LedgerGroupByLegderTypeLoadTask(Integer code) {
        this.code = code;
    }

    @Override
    protected List<LedgerGroup> call() throws Exception {
        try {
            LedgerGroupService service = EmcsAppContext.getContext().getBean(LedgerGroupService.class);
            List<LedgerGroup> list = service.findByLedgerType(code);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("LedgerGroup fetch", e);
        }
        return null;
    }
}
