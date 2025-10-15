package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.repository.BonusRepository;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BonusRegisterAllExcelTask extends Task<List<Map<String, Object>>> {
    private String societyCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String memberCode;
    private String locale;
    private Integer paymentMode;
    private Integer bonusType;
    private String bankCode;


    public BonusRegisterAllExcelTask(String societyCode, String memberCode, String locale, Integer paymentMode, String bankCode,
                                     LocalDate fromDate, LocalDate toDate, Integer bonusType) {
        this.societyCode = societyCode;
        this.memberCode = memberCode;
        this.locale = locale;
        this.paymentMode = paymentMode;
        this.bankCode = bankCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.bonusType = bonusType;
    }

    public BonusRegisterAllExcelTask() {

    }

    @Override
    protected List<Map<String, Object>> call() throws Exception {
        try {
            BonusRepository bonusRepository = EmcsAppContext.getContext().getBean(BonusRepository.class);
            List<Map<String, Object>> list = bonusRepository.findAllExcel(societyCode, fromDate, toDate, memberCode, locale, bankCode, paymentMode, bonusType);

            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
