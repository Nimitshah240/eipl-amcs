package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.dto.FinalizeDto;
import com.eipl.amcs.operation.billing.service.MemberBillService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class MemberBillDisburseLoadTask extends Task<Object> {

    private final FinalizeDto dto;
    public MemberBillDisburseLoadTask(FinalizeDto dto) {
        this.dto = dto;
    }
    @Override
    protected Object call() throws Exception {
        try {
            SocietyPaymentCycle paymentCycle = dto.getPaymentCycle();
            List<String> memberList = dto.getMemberCodeList();
            MemberBillService service = EmcsAppContext.getContext().getBean(MemberBillService.class);
            Object response = service.disburse(paymentCycle, memberList, CommonUtil.setIdentityHeader());
//
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_BILLING +"/disburse";
//            ResponseEntity<Boolean> response =
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), Boolean.class);
//            if (response == null)
//                return null;
//            return response.getBody();
            return response;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
