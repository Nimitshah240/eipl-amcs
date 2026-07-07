package com.eipl.amcs.report.task;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.ProductStockValuation;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.master.account.repository.ProductStockValuationRepository;
import javafx.concurrent.Task;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockValuationTask extends Task<List<ProductStockValuation>> {
    private final String societyCode;
    private final LocalDate asOnDate;
    private final String locale;
    private String productCode;
    private LedgerRepository ledgerRepository;
    private NextCodeRepository nextCodeRepository;
    private ProductStockValuationRepository productStockValuationRepository;


    public StockValuationTask(String societyCode, LocalDate asOnDate, String locale) {
        this.societyCode = societyCode;
        this.asOnDate = asOnDate;
        this.locale = locale;

    }

    public StockValuationTask(String societyCode, LocalDate asOnDate, String locale, String productCode) {
        this.societyCode = societyCode;
        this.asOnDate = asOnDate;
        this.locale = locale;
        this.productCode = productCode;
    }

    @Override
    protected List<ProductStockValuation> call() throws Exception {
        try {
            ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
            nextCodeRepository = EmcsAppContext.getContext().getBean(NextCodeRepository.class);
            productStockValuationRepository = EmcsAppContext.getContext().getBean(ProductStockValuationRepository.class);
            List<ProductStockValuation> list = new ArrayList<>();
            if (productCode == null) {
                list = fetchStockValuation(Date.valueOf(asOnDate), societyCode, locale);
            } else {
                list = fetchStockValuationFifo(Date.valueOf(asOnDate), societyCode, locale, productCode);
            }
            if (list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ProductStockValuation> fetchStockValuation(Date endDate, String societyCode, String locale) {
        List<ProductStockValuation> list = new ArrayList<>();
        productStockValuationRepository.deleteByGeneratedAt(endDate.toLocalDate());
        List<Object[]> listCurrentStock = ledgerRepository.fetchCurrentStockByProduct(endDate, societyCode, locale);
        listCurrentStock.forEach(item -> {
            double stockValue = 0;
            ProductStockValuation productStockValuation = new ProductStockValuation();
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
                productStockValuation = new ProductStockValuation((String) item[0], (String) item[1], Double.parseDouble(item[2].toString()), stockValue, (String) item[3], endDate.toLocalDate(), nextCodeRepository.getNextCode("ProductStockValuation", "productStockValuationCode", societyCode, 0));
                list.add(productStockValuation);
            } else {
                productStockValuation = new ProductStockValuation((String) item[0], (String) item[1], 0, 0, (String) item[3], endDate.toLocalDate(), nextCodeRepository.getNextCode("ProductStockValuation", "productStockValuationCode", societyCode, 0));
                list.add(productStockValuation);
            }
            productStockValuationRepository.save(productStockValuation);

        });
        return list;
    }

    private List<ProductStockValuation> fetchStockValuationFifo(Date endDate, String societyCode, String locale, String productCode) {
        List<ProductStockValuation> list = new ArrayList<>();
        productStockValuationRepository.deleteByGeneratedAt(endDate.toLocalDate());
        List<Object[]> listCurrentStock = ledgerRepository.fetchCurrentStockByProductFifo(endDate, societyCode, locale, productCode);
        listCurrentStock.forEach(item -> {
            ProductStockValuation productStockValuation = new ProductStockValuation();
            productStockValuation = new ProductStockValuation((String) item[0], (String) item[1], Double.parseDouble(item[2].toString()), Double.parseDouble(item[6].toString()), (String) item[3], endDate.toLocalDate(), nextCodeRepository.getNextCode("ProductStockValuation", "productStockValuationCode", societyCode, 0));
            list.add(productStockValuation);
            productStockValuationRepository.save(productStockValuation);
        });
        return list;
    }
}
