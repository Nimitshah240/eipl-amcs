package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.report.dto.LedgerClose;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SubLedgerCloseTask extends Task<List<LedgerClose>> {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;
    private String ledgerCode;


    public SubLedgerCloseTask(String ledgerCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.ledgerCode = ledgerCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    public SubLedgerCloseTask() {

    }

    @Override
    protected List<LedgerClose> call() throws Exception {
        try {
            LedgerRepository ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
            List<LedgerClose> listResp = new ArrayList<>();
            Date fromDt = Date.valueOf(fromDate);
            Date toDt = Date.valueOf(toDate);
            List<Object[]> list = ledgerRepository.fetchSubLedgerOpeningBalance(ledgerCode, fromDt, toDt, locale);
            if (list != null && !list.isEmpty()) {
                for (Object[] arr : list) {
                    LedgerClose bal = new LedgerClose((String) arr[0], (String) arr[1], (((BigDecimal) arr[2]).doubleValue() < 0 ? false : true), ((BigDecimal) arr[2]).doubleValue());
                    listResp.add(bal);
                }
            }
            return listResp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
