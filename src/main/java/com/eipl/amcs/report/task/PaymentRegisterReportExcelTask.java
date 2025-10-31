package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.repository.MemberBillRepository;
import com.eipl.amcs.report.dto.PaymentForBank;
import javafx.concurrent.Task;

import java.util.List;

public class PaymentRegisterReportExcelTask extends Task<List<PaymentForBank>> {
    private final String societyCode;
    private final String societyPaymentCycleCode;
    private final String locale;
    private final Integer paymentMode;
    private final String bankCode;


    public PaymentRegisterReportExcelTask(String societyCode, String societyPaymentCycleCode, String locale, Integer paymentMode, String bankCode) {
        this.societyCode = societyCode;
        this.societyPaymentCycleCode = societyPaymentCycleCode;
        this.locale = locale;
        this.paymentMode = paymentMode;
        this.bankCode = bankCode;
    }

    @Override
    protected List<PaymentForBank> call() throws Exception {
        try {
            MemberBillRepository billRepository = EmcsAppContext.getContext().getBean(MemberBillRepository.class);

            List<PaymentForBank> list = billRepository.findPaymentRegisterReportExcel(societyCode, societyPaymentCycleCode, paymentMode, bankCode, locale);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
