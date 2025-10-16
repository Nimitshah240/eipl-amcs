package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;
import com.eipl.amcs.operation.procurement.dto.MilkRateAndDetailsDto;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class MilkDispatchRateAndDetailsLoadTask extends Task<MilkDispatchRateAndDetailsDto> {
    private String code;

    public MilkDispatchRateAndDetailsLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected MilkDispatchRateAndDetailsDto call() throws Exception {
        try {
            SocietyMilkPurchaseRateService service= EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);;
            MilkDispatchRateAndDetailsDto milkDispatchRateAndDetailsDto =service.fetchRateAndDetails(code);

            if (milkDispatchRateAndDetailsDto==null||milkDispatchRateAndDetailsDto.getDetails().isEmpty())
                return null;

            return milkDispatchRateAndDetailsDto;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY_MILK_PURCHASE_RATE + "/rate-and-details/{code}";
//            Map<String, String> uriVariable = new HashMap<>();
//            uriVariable.put("code", code);
//            ResponseEntity<MilkDispatchRateAndDetailsDto> response = restTemplate.exchange(url, HttpMethod.GET, null, MilkDispatchRateAndDetailsDto.class, uriVariable);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
