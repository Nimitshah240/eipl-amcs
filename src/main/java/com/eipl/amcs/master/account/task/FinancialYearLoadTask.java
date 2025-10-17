package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.service.FinancialYearService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FinancialYearLoadTask extends Task<List<FinancialYear>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialYearLoadTask.class);

    @Override
    protected List<FinancialYear> call() throws Exception {
        try {
            FinancialYearService financialYearService = EmcsAppContext.getContext().getBean(FinancialYearService.class);
            return financialYearService.findAll();
        } catch (Exception e) {
            LOGGER.error("FinancialYear fetch", e);
        }
        return null;
    }
}
