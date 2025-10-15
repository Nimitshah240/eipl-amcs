package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradingTask extends Task<List<LedgerBalance>> {
    private String societyCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;
    private LedgerRepository ledgerRepository;


    public TradingTask(String societyCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    public TradingTask() {

    }

    @Override
    protected List<LedgerBalance> call() throws Exception {
        try {
            ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
            List<LedgerBalance> list = fetchTrading(societyCode, CommonUtils.convertToSqlDate(fromDate), CommonUtils.convertToSqlDate(toDate), locale);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<LedgerBalance> fetchTrading(String societyCode, Date fromDate, Date toDate, String locale) {
        double stockValuation = 0;
        try {
            List<ProductStockValuation> listStockValuation = fetchStockValuation(toDate, societyCode, locale);
            if (listStockValuation != null)
                stockValuation = listStockValuation.stream().mapToDouble(m -> m.getValuation()).sum();

            List<Object[]> list = ledgerRepository.fetchTrading(societyCode, fromDate, toDate, locale);
            if (list == null || list.isEmpty()) {
                List<LedgerBalance> listResp = new ArrayList<>();
                listResp.add(new LedgerBalance("", "stockvaluation", 0, stockValuation, stockValuation));
                return listResp;
            } else if (list != null && !list.isEmpty()) {
                List<LedgerBalance> listResp = new ArrayList<>();
                list.forEach(item -> {
                    listResp.add(new LedgerBalance((String) item[0], (String) item[1], Double.parseDouble(item[3].toString()), Double.parseDouble(item[2].toString()), Double.parseDouble(item[4].toString())));
                });
                listResp.add(new LedgerBalance("", "stockvaluation", 0, stockValuation, stockValuation));
                return listResp;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ProductStockValuation> fetchStockValuation(Date endDate, String societyCode, String locale) {
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
                        stockValue = stockValue + (stock * Double.parseDouble(arrReceipt[1].toString()));
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
