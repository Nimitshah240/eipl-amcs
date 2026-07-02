package com.eipl.amcs.report.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.repository.MemberBillRepository;
import com.eipl.amcs.report.dto.PaymentForBank;
import com.eipl.amcs.report.dto.PaymentForBankProjection;
import javafx.concurrent.Task;

import java.util.ArrayList;
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

            List<PaymentForBankProjection> list = billRepository.findPaymentRegisterReportExcel(societyCode, societyPaymentCycleCode, paymentMode, bankCode, locale);
            if (list == null || list.isEmpty())
                return null;
            List<PaymentForBank> result = new ArrayList<>();

            for (PaymentForBankProjection r : list) {
                PaymentForBank dto = new PaymentForBank();

                dto.setSr_no(r.getSr_no());
                dto.setSoc_code(r.getSoc_code());
                dto.setSoc_name(r.getSoc_name());
                dto.setPayment_period(r.getPayment_period());
                dto.setMember_Code(r.getMember_code());
                dto.setMember_name(r.getMember_name());
                dto.setBank_acno(r.getBank_acno());
                dto.setIfsc(r.getIfsc());
                dto.setBank_name(r.getBank_name());
                dto.setBranch_name(r.getBranch_name());
                dto.setNet_amount(r.getNet_amount());
                dto.setMilk_amount(r.getMilk_amount());
                dto.setOther_ded_amount(r.getOther_ded_amount());

                result.add(dto);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
