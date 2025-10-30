package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.repository.BonusRepository;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BonusRegisterReportExcelTask extends Task<List<Map<String, Object>>> {
    private final String societyCode;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String memberCode;
    private final String locale;
    private final String milkTypeCode;
    private final Integer paymentMode;
    private final Integer bonusType;
    private final String bankCode;


    public BonusRegisterReportExcelTask(String societyCode, String memberCode, String locale, Integer paymentMode, String bankCode,
                                        LocalDate fromDate, LocalDate toDate, Integer bonusType, String milkTypeCode) {
        this.societyCode = societyCode;
        this.memberCode = memberCode;
        this.locale = locale;
        this.paymentMode = paymentMode;
        this.bankCode = bankCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.bonusType = bonusType;
        this.milkTypeCode = milkTypeCode;
    }

    @Override
    protected List<Map<String, Object>> call() throws Exception {
        try {
            BonusRepository bonusRepository = EmcsAppContext.getContext().getBean(BonusRepository.class);
            List<Map<String, Object>> list = bonusRepository.findAllBonus(societyCode, fromDate, toDate, memberCode, locale, bankCode, paymentMode, bonusType, milkTypeCode);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
