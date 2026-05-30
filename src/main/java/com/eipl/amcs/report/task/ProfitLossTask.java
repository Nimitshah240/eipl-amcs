package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.master.account.repository.ProductStockValuationRepository;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfitLossTask extends Task<List<LedgerBalance>> {
    private final String societyCode;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String locale;
    private LedgerRepository ledgerRepository;


    public ProfitLossTask(String societyCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    @Override
    protected List<LedgerBalance> call() throws Exception {
        try {
            List<LedgerBalance> list = new ArrayList<>();
            ledgerRepository = EmcsAppContext.getContext().getBean(LedgerRepository.class);
            List<LedgerBalance> listExpense = fetchProfitLoss(societyCode, CommonUtils.convertToSqlDate(fromDate), CommonUtils.convertToSqlDate(toDate), 0, locale);
            if (listExpense == null)
                listExpense = new ArrayList<>();
//                return null;
            listExpense.forEach(item -> item.setIncomeExpense(0));
            list.addAll(listExpense);

            List<LedgerBalance> listIncome = fetchProfitLoss(societyCode, CommonUtils.convertToSqlDate(fromDate), CommonUtils.convertToSqlDate(toDate), 1, locale);
            if (listIncome == null)
                listIncome = new ArrayList<>();
//                return null;
            listIncome.forEach(item -> item.setIncomeExpense(1));
            list.addAll(listIncome);

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LedgerBalance> fetchProfitLoss(String societyCode, Date fromDate, Date toDate, int incomeExpense, String locale) {
        double tradingProfit = 0;
        try {
            if (incomeExpense == 1 && tradingProfit == 0) {
                List<LedgerBalance> listTrading = fetchTrading(societyCode, fromDate, toDate, locale);
                if (listTrading != null) tradingProfit = listTrading.stream().mapToDouble(m -> m.getBalance()).sum();
            }
            List<Object[]> list = ledgerRepository.fetchProfitLoss(societyCode, fromDate, toDate, incomeExpense, locale);
            List<LedgerBalance> listResp = new ArrayList<>();

            if (list != null && !list.isEmpty()) {
                list.forEach(item -> {
                    listResp.add(new LedgerBalance((String) item[0], (String) item[1], 0, 0, ((BigDecimal) item[2]).doubleValue()));
                });
                if (incomeExpense == 1 && tradingProfit > 0)
                    listResp.add(new LedgerBalance("", "trading", 0, 0, tradingProfit));
                else if (incomeExpense == 0 && tradingProfit < 0)
                    listResp.add(new LedgerBalance("", "trading", 0, 0, tradingProfit));
            } else {
                if (incomeExpense == 1 && tradingProfit > 0) {
                    listResp.add(new LedgerBalance("", "trading", 0, 0, tradingProfit));
                } else if (incomeExpense == 0 && tradingProfit < 0) {
                    listResp.add(new LedgerBalance("", "trading", 0, 0, tradingProfit));
                }
            }
            return listResp;
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
            for (com.eipl.amcs.master.account.model.ProductStockValuation psv : productStockValuationList) {
                listStockValuation.add(new com.eipl.amcs.report.dto.ProductStockValuation(psv.getProductCode(), psv.getProductName(), psv.getStock(), psv.getValuation(), psv.getUnit()));
            }
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
