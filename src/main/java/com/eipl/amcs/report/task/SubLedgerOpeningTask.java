package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;

public class SubLedgerOpeningTask extends Task<List<Object[]>> {
    private String societyCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;


    public SubLedgerOpeningTask(String societyCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    public SubLedgerOpeningTask() {

    }

    @Override
    protected List<Object[]> call() throws Exception {
        try {
            LedgerRepository ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
            List<Object[]> listStockValuation = ledgerRepository.fetchSubLedgerOpeningBalanceSecond(societyCode, fromDate, toDate, locale);
            if (listStockValuation == null || listStockValuation.isEmpty()) {
                return null;
            }
            return listStockValuation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
