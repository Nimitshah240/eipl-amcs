package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BalanceSheetTask extends Task<List<LedgerBalance>> {
    private final String societyCode;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String locale;


    public BalanceSheetTask(String societyCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    @Override
    protected List<LedgerBalance> call() throws Exception {
        try {
            LedgerRepository ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);

            List<Object[]> listObject = ledgerRepository.fetchBalanceSheet(societyCode, CommonUtils.convertToSqlDate(fromDate), CommonUtils.convertToSqlDate(toDate), 0, locale);
            List<LedgerBalance> listResp = new ArrayList<>();
            if (listObject == null)
                listObject = new ArrayList<>();

            double stockValuation = 0;
            List<ProductStockValuation> listStockValuation = fetchStockValuation(Date.valueOf(toDate), societyCode, locale);
            if (listStockValuation != null)
                stockValuation = listStockValuation.stream().mapToDouble(m -> m.getValuation()).sum();


            listObject.forEach(item -> {
                listResp.add(new LedgerBalance((String) item[0], (String) item[1], 0, 0, ((BigDecimal) item[2]).doubleValue()));
            });
            listResp.add(new LedgerBalance("", "stockvaluation", 0, stockValuation, stockValuation));

            listResp.forEach(item -> item.setIncomeExpense(0));
            List<LedgerBalance> list = new ArrayList<>(listResp);
            listResp.clear();
            List<Object[]> listObject1 = ledgerRepository.fetchBalanceSheet(societyCode, CommonUtils.convertToSqlDate(fromDate), CommonUtils.convertToSqlDate(toDate), 1, locale);
            if (listObject1 == null)
                listObject1 = new ArrayList<>();

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


    private List<ProductStockValuation> fetchStockValuation(Date endDate, String societyCode, String locale) {

        LedgerRepository ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);

        List<ProductStockValuation> list = new ArrayList<>();
        List<Object[]> listCurrentStock = ledgerRepository.fetchCurrentStockByProduct(endDate, societyCode, locale);
        listCurrentStock.forEach(item -> {
            double stockValue = 0;
            List<Object[]> listReceipt = ledgerRepository.fetchProductReceipt((String) item[0], endDate);
            if (listReceipt != null && !listReceipt.isEmpty()) {

                double stock = Double.parseDouble(item[2].toString());
                for (Object[] arrReceipt : listReceipt) {
                    if (stock <= 0) {
                        break;
                    }
                    if (Double.parseDouble(arrReceipt[1].toString()) >= stock) {
                        stockValue = stockValue + (stock * Double.parseDouble(arrReceipt[0].toString()));
                        break;
                    } else {
                        stockValue = Double.parseDouble(arrReceipt[1].toString()) * Double.parseDouble(arrReceipt[0].toString());
                        stock -= Double.parseDouble(arrReceipt[1].toString());
                    }
                }
                list.add(new ProductStockValuation((String) item[0], (String) item[1], Double.parseDouble(item[2].toString()), stockValue, (String) item[3]));
            } else {
                list.add(new ProductStockValuation((String) item[0], (String) item[1], 0, 0, (String) item[3]));
            }
        });
        return list;
    }

}
