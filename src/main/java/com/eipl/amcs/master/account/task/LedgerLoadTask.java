package com.eipl.amcs.master.account.task;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.service.LedgerService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LedgerLoadTask extends Task<List<Ledger>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerLoadTask.class);
    private LedgerService ledgerService;

    @Override
    protected List<Ledger> call() throws Exception {
        try {

            List<Ledger> list = ledgerService.findAllByIsActive();
            return list;
        } catch (Exception e) {
            LOGGER.error("Ledger fetch", e);
        }
        return null;
    }
}
