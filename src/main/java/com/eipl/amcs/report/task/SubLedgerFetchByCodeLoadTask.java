package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.service.SubLedgerService;
import javafx.concurrent.Task;

import java.util.Optional;

public class SubLedgerFetchByCodeLoadTask extends Task<SubLedger> {
    private String ledgerCode;

    public SubLedgerFetchByCodeLoadTask(String ledgerCode) {
        this.ledgerCode = ledgerCode;

    }

    public SubLedgerFetchByCodeLoadTask() {

    }

    @Override
    protected SubLedger call() throws Exception {
        try {
            SubLedgerService service = EmcsAppContext.getContext().getBean(SubLedgerService.class);
            Optional<SubLedger> subLedgerOptional = service.findById(ledgerCode);
            return subLedgerOptional.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}