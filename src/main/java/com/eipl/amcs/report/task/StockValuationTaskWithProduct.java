package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.report.dto.ProductStockValuation;
import javafx.concurrent.Task;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockValuationTaskWithProduct extends Task<List<ProductStockValuation>> {
    private final String societyCode;
    private final LocalDate asOnDate;
    private final String locale;
    private final String productCode;
    private LedgerRepository ledgerRepository;


    public StockValuationTaskWithProduct(String societyCode, LocalDate asOnDate, String locale, String productCode) {
        this.societyCode = societyCode;
        this.asOnDate = asOnDate;
        this.locale = locale;
        this.productCode = productCode;

    }

    @Override
    protected List<ProductStockValuation> call() throws Exception {
        try {
            ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
            List<ProductStockValuation> list = fetchStockValuationWithProduct(Date.valueOf(asOnDate), societyCode, locale, productCode);
            if (list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ProductStockValuation> fetchStockValuationWithProduct(Date endDate, String societyCode, String locale, String productCode) {
        List<ProductStockValuation> list = new ArrayList<>();
        List<Object[]> listCurrentStock = ledgerRepository.fetchCurrentStockByProductWithProduct(endDate, societyCode, locale, productCode);
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
                        stockValue = stockValue +  Double.parseDouble(arrReceipt[1].toString()) * Double.parseDouble(arrReceipt[0].toString());
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
