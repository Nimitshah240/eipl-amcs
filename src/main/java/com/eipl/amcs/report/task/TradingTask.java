package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.master.account.repository.ProductStockValuationRepository;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FormatterFactory;
import javafx.concurrent.Task;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradingTask extends Task<List<LedgerBalance>> {
    private final String societyCode;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String locale;
    private LedgerRepository ledgerRepository;


    public TradingTask(String societyCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    @Override
    protected List<LedgerBalance> call() throws Exception {
        try {
            ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
            List<LedgerBalance> list = fetchTrading(societyCode, CommonUtils.convertToSqlDate(fromDate), CommonUtils.convertToSqlDate(toDate), locale);
            if (list == null)
                list = new ArrayList<>();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<LedgerBalance> fetchTrading(String societyCode, Date fromDate, Date toDate, String locale) {
        double stockValuation = 0;
        try {
            ProductStockValuationRepository productStockValuationRepository = EmcsAppContext.getContext().getBean(ProductStockValuationRepository.class);
            List<com.eipl.amcs.master.account.model.ProductStockValuation> productStockValuationList = productStockValuationRepository.findAllByNearestDate(toDate.toLocalDate());
            List<ProductStockValuation> listStockValuation = new ArrayList<>();
            LocalDate generatedDate = LocalDate.now();
            for (com.eipl.amcs.master.account.model.ProductStockValuation psv : productStockValuationList) {
                generatedDate = psv.getGeneratedAt();
                listStockValuation.add(new com.eipl.amcs.report.dto.ProductStockValuation(psv.getProductCode(), psv.getProductName(), psv.getStock(), psv.getValuation(), psv.getUnit()));
            }

            if (listStockValuation != null)
                stockValuation = listStockValuation.stream().mapToDouble(m -> m.getValuation()).sum();

            List<Object[]> list = ledgerRepository.fetchTrading(societyCode, fromDate, toDate, locale);
            if (list == null || list.isEmpty()) {
                List<LedgerBalance> listResp = new ArrayList<>();
                listResp.add(new LedgerBalance("", "stockvaluation as on " + FormatterFactory.formatDate(generatedDate, MainApp.getLocale()), 0, stockValuation, stockValuation));
                return listResp;
            } else if (list != null && !list.isEmpty()) {
                List<LedgerBalance> listResp = new ArrayList<>();
                list.forEach(item -> {
                    listResp.add(new LedgerBalance((String) item[0], (String) item[1], Double.parseDouble(item[3].toString()), Double.parseDouble(item[2].toString()), Double.parseDouble(item[4].toString())));
                });
                listResp.add(new LedgerBalance("", "stockvaluation as on " + FormatterFactory.formatDate(generatedDate, MainApp.getLocale()), 0, stockValuation, stockValuation));
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
                        stockValue = stockValue + (stock * Double.parseDouble(arrReceipt[0].toString()));
                        break;
                    } else {
                        stockValue = stockValue + Double.parseDouble(arrReceipt[1].toString()) * Double.parseDouble(arrReceipt[0].toString());
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
