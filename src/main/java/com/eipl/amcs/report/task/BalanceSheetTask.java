package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BalanceSheetTask extends Task<List<LedgerBalance>> {
    private String societyCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;


    public BalanceSheetTask(String societyCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    public BalanceSheetTask() {

    }

    @Override
    protected List<LedgerBalance> call() throws Exception {
        try {
            LedgerRepository ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);

            List<Object[]> listObject = ledgerRepository.fetchBalanceSheet(societyCode, CommonUtils.convertToSqlDate(fromDate), CommonUtils.convertToSqlDate(toDate), 0, locale);
            List<LedgerBalance> listResp = new ArrayList<>();
            if (listObject == null || listObject.isEmpty())
                return null;
            listObject.forEach(item -> {
                listResp.add(new LedgerBalance((String) item[0], (String) item[1], 0, 0, ((BigDecimal) item[2]).doubleValue()));
            });

            listResp.forEach(item -> item.setIncomeExpense(0));
            List<LedgerBalance> list = new ArrayList<>(listResp);

            listResp.clear();
            List<Object[]> listObject1 = ledgerRepository.fetchBalanceSheet(societyCode, CommonUtils.convertToSqlDate(fromDate), CommonUtils.convertToSqlDate(toDate), 1, locale);
            if (listObject1 == null || listObject1.isEmpty())
                return null;
            listObject1.forEach(item -> {
                listResp.add(new LedgerBalance((String) item[0], (String) item[1], 0, 0, ((BigDecimal) item[2]).doubleValue()));
            });
            listResp.forEach(item -> item.setIncomeExpense(1));
            list.addAll(listResp);

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
