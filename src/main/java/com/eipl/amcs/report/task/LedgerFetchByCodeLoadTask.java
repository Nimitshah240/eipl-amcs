package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.service.LedgerService;
import javafx.concurrent.Task;

import java.util.Optional;

public class LedgerFetchByCodeLoadTask extends Task<Ledger> {
    private String ledgerCode;

    public LedgerFetchByCodeLoadTask(String ledgerCode) {
        this.ledgerCode = ledgerCode;

    }

    public LedgerFetchByCodeLoadTask() {

    }

    @Override
    protected Ledger call() throws Exception {
        try {
            LedgerService service = EmcsAppContext.getContext().getBean(LedgerService.class);
            Optional<Ledger> ledgerOptional = service.findById(ledgerCode);
            return ledgerOptional.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}