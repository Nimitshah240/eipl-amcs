package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.LedgerOpeningBalanceService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RojmedOpeningBalanceLoadTask extends Task<BigDecimal> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RojmedOpeningBalanceLoadTask.class);
    private final LocalDate toDate;

    public RojmedOpeningBalanceLoadTask(LocalDate toDate) {
        this.toDate = toDate;
    }

    @Override
    protected BigDecimal call() throws Exception {
        try {
            LedgerOpeningBalanceService ledgerOpeningBalanceService = EmcsAppContext.getContext().getBean(LedgerOpeningBalanceService.class);
            return ledgerOpeningBalanceService.getLedgerOpeningBalanceOfTypeCash(toDate);
        } catch (Exception e) {
            LOGGER.error("ledger Number fetch", e);
        }
        return BigDecimal.ZERO;
    }
}
