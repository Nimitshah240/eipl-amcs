package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.repository.BonusRepository;
import com.eipl.amcs.report.dto.BonusRegister;
import javafx.concurrent.Task;

import java.util.List;

public class BonusRegisterReportLoadTask extends Task<List<BonusRegister>> {
    private final String societyCode;
    private final String bonusSummaryCode;


    public BonusRegisterReportLoadTask(String societyCode, String bonusSummaryCode) {
        this.societyCode = societyCode;
        this.bonusSummaryCode = bonusSummaryCode;
    }

    @Override
    protected List<BonusRegister> call() throws Exception {
        try {
            BonusRepository bonusRepository = EmcsAppContext.getContext().getBean(BonusRepository.class);
            List<BonusRegister> list = bonusRepository.findBonus(societyCode, bonusSummaryCode);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
