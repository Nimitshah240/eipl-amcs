package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.FinancialYearRepository;
import javafx.concurrent.Task;

public class FinancialYearsCodeTask extends Task<Boolean> {

    private final String code;

    public FinancialYearsCodeTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            FinancialYearRepository financialYearRepository = EmcsAppContext.getContext().getBean(FinancialYearRepository.class);
            Integer ints = financialYearRepository.fetchByCode(code);
            return ints <= 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
