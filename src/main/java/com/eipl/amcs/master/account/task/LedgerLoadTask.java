package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.service.LedgerService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LedgerLoadTask extends Task<List<Ledger>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerLoadTask.class);

    @Override
    protected List<Ledger> call() throws Exception {
        try {
            LedgerService ledgerService = EmcsAppContext.getContext().getBean(LedgerService.class);
            return ledgerService.findAllByIsActive();
        } catch (Exception e) {
            LOGGER.error("Ledger fetch", e);
        }
        return null;
    }
}
