package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.dto.FinalizeDto;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class MemberBillFinalizeLoadTask extends Task<Object> {

    private final FinalizeDto dto;

    public MemberBillFinalizeLoadTask(FinalizeDto dto) {
        this.dto = dto;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);
            SocietyPaymentCycle paymentCycle = dto.getPaymentCycle();
            List<String> memberList = dto.getMemberCodeList();
            if (MainApp.getProperty("client.code", "").equals("BANAS_AMCS")) {
                return service.finalize(paymentCycle, memberList, dto.getDeductionFromDate(), dto.getDeductionToDate());
            } else {
                return service.finalize(paymentCycle, memberList);
            }
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
