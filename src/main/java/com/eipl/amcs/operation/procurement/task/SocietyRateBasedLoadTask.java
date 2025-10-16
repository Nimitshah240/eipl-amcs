package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class SocietyRateBasedLoadTask extends Task<List<SocietyMilkPurchaseRateBased>> {
    private String code;

    public SocietyRateBasedLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<SocietyMilkPurchaseRateBased> call() throws Exception {
        try {

            SocietyMilkPurchaseRateService service=EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);;
            List<SocietyMilkPurchaseRateBased>list=(service.fetchRateBased(code));

            if (list==null||list.isEmpty())return null;
            return list;

//
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY_MILK_PURCHASE_RATE + "/based";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("code", code);
//
//            ResponseEntity<SocietyMilkPurchaseRateBased[]> response = restTemplate.getForEntity(builder.toUriString(), SocietyMilkPurchaseRateBased[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
