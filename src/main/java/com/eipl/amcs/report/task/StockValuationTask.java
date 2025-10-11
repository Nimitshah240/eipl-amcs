package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.report.dto.ProductStockValuation;
import javafx.concurrent.Task;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockValuationTask extends Task<List<ProductStockValuation>> {
    private String societyCode;
    private LocalDate asOnDate;
    private String locale;
    private LedgerRepository ledgerRepository;


    public StockValuationTask(String societyCode, LocalDate asOnDate, String locale) {
        this.societyCode = societyCode;
        this.asOnDate = asOnDate;
        this.locale = locale;

    }

    public StockValuationTask() {

    }

    @Override
    protected List<ProductStockValuation> call() throws Exception {
        try {
            ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);

            List<ProductStockValuation> list = fetchStockValuation(Date.valueOf(asOnDate), societyCode, locale);
            if (list.isEmpty())
                return null;
            return list;
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
