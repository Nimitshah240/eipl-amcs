package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.report.dto.ProductStockValuationWithSaleAndPurchase;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockValuationTaskWithSaleAndPurchase extends Task<List<ProductStockValuationWithSaleAndPurchase>> {
    private String societyCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;
    private String productCode;
    private LedgerRepository ledgerRepository;


    public StockValuationTaskWithSaleAndPurchase(String societyCode, LocalDate fromDate, LocalDate toDate, String locale, String productCode) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
        this.productCode = productCode;

    }

    public StockValuationTaskWithSaleAndPurchase() {

    }

    @Override
    protected List<ProductStockValuationWithSaleAndPurchase> call() throws Exception {
        try {
            ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
            List<ProductStockValuationWithSaleAndPurchase> list = fetchStockValuationWithSaleAndPurchase(CommonUtils.convertToSqlDate(fromDate), CommonUtils.convertToSqlDate(toDate), societyCode, locale, productCode);
            if (list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ProductStockValuationWithSaleAndPurchase> fetchStockValuationWithSaleAndPurchase(Date fromDate, Date toDate, String societyCode, String locale, String productCode) {
        List<ProductStockValuation> list = new ArrayList<>();
        List<Object[]> listCurrentStock = ledgerRepository.fetchCurrentStockByProductWithProduct(Date.valueOf(fromDate.toLocalDate().minusDays(1)), societyCode, locale, productCode);
        List<Map<String, Object>> listPurchase = ledgerRepository.fetchPurchaseProduct(fromDate, toDate, societyCode, locale, productCode);
        List<Map<String, Object>> listSale = ledgerRepository.fetchSaleProduct(fromDate, toDate, societyCode, locale, productCode);
        Map<String, Map<String, Object>> listPurchaseMap = new HashMap<>();
        for (Map<String, Object> map : listPurchase) {
            listPurchaseMap.put(String.valueOf(map.get("productCode")), map);
        }
        Map<String, Map<String, Object>> listSaleMap = new HashMap<>();
        for (Map<String, Object> map : listSale) {
            listSaleMap.put(String.valueOf(map.get("productCode")), map);
        }

        listCurrentStock.forEach(item -> {
            double stockValue = 0;
            List<Object[]> listReceipt = ledgerRepository.fetchProductReceipt((String) item[0], fromDate);
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

        List<ProductStockValuationWithSaleAndPurchase> finalList = new ArrayList<>();
        for (ProductStockValuation productStockValuation : list) {
            ProductStockValuationWithSaleAndPurchase obj = new ProductStockValuationWithSaleAndPurchase();
            obj.setProductCode(productStockValuation.getProductCode());
            obj.setProductName(productStockValuation.getProductName());
            obj.setStock(productStockValuation.getStock());
            obj.setValuation(productStockValuation.getValuation());
            obj.setUnit(productStockValuation.getUnit());

            if (listPurchaseMap.get(productStockValuation.getProductCode()) != null) {
                obj.setPurchaseProductCode(productStockValuation.getProductCode());
                obj.setPurchaseProductName(productStockValuation.getProductName());
                obj.setPurchaseStock(Double.parseDouble(String.valueOf(listPurchaseMap.get(productStockValuation.getProductCode()).get("stock"))));
                obj.setPurchaseValuation(Double.parseDouble(String.valueOf(listPurchaseMap.get(productStockValuation.getProductCode()).get("valuation"))));
                obj.setPurchaseUnit(productStockValuation.getUnit());
            }
            if (listSaleMap.get(productStockValuation.getProductCode()) != null) {
                obj.setSaleProductCode(productStockValuation.getProductCode());
                obj.setSaleProductName(productStockValuation.getProductName());
                obj.setSaleStock(Double.parseDouble(String.valueOf(listSaleMap.get(productStockValuation.getProductCode()).get("stock"))));
                obj.setSaleValuation(Double.parseDouble(String.valueOf(listSaleMap.get(productStockValuation.getProductCode()).get("valuation"))));
                obj.setSaleUnit(productStockValuation.getUnit());
            }
            obj.setTotalStock(obj.getStock() + obj.getPurchaseStock());
            obj.setTotalValuation(obj.getValuation() + obj.getPurchaseValuation());
            obj.setTotalProductCode(obj.getProductCode());
            obj.setTotalProductName(obj.getProductName());
            obj.setTotalUnit(obj.getUnit());


            obj.setClosingStock(obj.getStock() + obj.getPurchaseStock() - obj.getSaleStock());
            obj.setClosingValuation(obj.getValuation() + obj.getPurchaseValuation() - obj.getSaleValuation());
            obj.setClosingProductCode(obj.getProductCode());
            obj.setClosingProductName(obj.getProductName());
            obj.setClosingUnit(obj.getUnit());
            finalList.add(obj);
        }
        return finalList;
    }


}
