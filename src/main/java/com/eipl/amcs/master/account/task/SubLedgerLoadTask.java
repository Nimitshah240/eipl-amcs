package com.eipl.amcs.master.account.task;

import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.service.SubLedgerService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SubLedgerLoadTask extends Task<List<SubLedger>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLedgerLoadTask.class);
    private SubLedgerService subLedgerService;

    @Override
    protected List<SubLedger> call() throws Exception {
        try {


            List<SubLedger> list = subLedgerService.findAll();
            if (list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("SubLedger fetch", e);
        }
        return null;
    }
}
