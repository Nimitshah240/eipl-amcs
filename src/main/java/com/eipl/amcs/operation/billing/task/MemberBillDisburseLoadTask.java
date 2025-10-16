package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.dto.FinalizeDto;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class MemberBillDisburseLoadTask extends Task<Object> {

    private final FinalizeDto dto;

    public MemberBillDisburseLoadTask(FinalizeDto dto) {
        this.dto = dto;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);

            SocietyPaymentCycle paymentCycle = dto.getPaymentCycle();
            List<String> memberList = dto.getMemberCodeList();

            service.disburse(paymentCycle, memberList, CommonUtil.setIdentityHeader());
//
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_BILLING +"/disburse";
//            ResponseEntity<Boolean> response =
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), Boolean.class);
//            if (response == null)
//                return null;
//            return response.getBody();
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
