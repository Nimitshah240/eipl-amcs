package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.FinancialYearRepository;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;

public class NextFinYearDateTask extends Task<List<LocalDate>> {

    private LocalDate currentDate;

    public NextFinYearDateTask(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public NextFinYearDateTask() {
    }

    @Override
    protected List<LocalDate> call() throws Exception {
        try {
            FinancialYearRepository financialYearRepository = EmcsAppContext.getContext().getBean(FinancialYearRepository.class);
            List<LocalDate> list = financialYearRepository.fetchByDate(currentDate);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
