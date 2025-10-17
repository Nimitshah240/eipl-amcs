package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

public class MemberTotalAmountLoadTask extends Task<BigDecimal> {
    private String societyPaymentCycleCode;
    private String memberCode;

        public MemberTotalAmountLoadTask(String societyPaymentCycleCode, String memberCode) {
            this.societyPaymentCycleCode = societyPaymentCycleCode;
            this.memberCode = memberCode;
        }

    @Override
    protected BigDecimal call() throws Exception {
        try {
            MilkCollectionService service=EmcsAppContext.getContext().getBean(MilkCollectionService.class);
          BigDecimal response =  service.findTotalAmount(societyPaymentCycleCode, memberCode);
            if (response == null) return null;
           return response;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/total_amount";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("societyPaymentCycleCode", societyPaymentCycleCode)
//                    .queryParam("memberCode", memberCode);
//            ResponseEntity<BigDecimal> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
//                    null, BigDecimal.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
