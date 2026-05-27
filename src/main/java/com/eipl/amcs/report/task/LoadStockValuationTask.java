package com.eipl.amcs.report.task;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.master.account.repository.ProductStockValuationRepository;
import com.eipl.amcs.master.account.model.ProductStockValuation;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;

public class LoadStockValuationTask extends Task<List<ProductStockValuation>> {
    private final String societyCode;
    private final LocalDate asOnDate;
    private final String locale;
    private LedgerRepository ledgerRepository;
    private NextCodeRepository nextCodeRepository;
    private ProductStockValuationRepository productStockValuationRepository;


    public LoadStockValuationTask(String societyCode, LocalDate asOnDate, String locale) {
        this.societyCode = societyCode;
        this.asOnDate = asOnDate;
        this.locale = locale;

    }

    @Override
    protected List<ProductStockValuation> call() throws Exception {
        try {
            productStockValuationRepository = EmcsAppContext.getContext().getBean(ProductStockValuationRepository.class);
            List<ProductStockValuation> list = productStockValuationRepository.findAllByNearestDate(asOnDate);

            if (list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
