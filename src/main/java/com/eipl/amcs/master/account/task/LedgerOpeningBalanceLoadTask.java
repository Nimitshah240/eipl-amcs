package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.service.LedgerOpeningBalanceService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LedgerOpeningBalanceLoadTask extends Task<List<LedgerOpeningBalance>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerOpeningBalanceLoadTask.class);

    @Override
    protected List<LedgerOpeningBalance> call() throws Exception {
        try {
            LedgerOpeningBalanceService service = EmcsAppContext.getContext().getBean(LedgerOpeningBalanceService.class);
            List<LedgerOpeningBalance> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("LedgerOpeningBalance fetch", e);
        }
        return null;
    }
}
