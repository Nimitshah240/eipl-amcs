package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateViewTask extends Task<List<String>> {
    private short rateType;
    private String code;
    private Integer milkType;
    private Integer milkQualityType;

    private static final Logger LOGGER = LoggerFactory.getLogger(RateViewTask.class);

    public RateViewTask(short rateType, String code, Integer milkType, Integer milkQualityType) {
        this.rateType = rateType;
        this.code = code;
        this.milkType = milkType;
        this.milkQualityType = milkQualityType;
    }

    @Override
    protected List<String> call() throws Exception {
        try {
            List<String> listStr = null;
            if(rateType == (short) 0) {
                RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_MILK_PURCHASE_RATE + "/view/{code}/{milkTypeCode}/{milkQualityTypeCode}";
                Map<String, Object> uriVariables = new HashMap<>();
                uriVariables.put("code", code);
                uriVariables.put("milkTypeCode", milkType);
                uriVariables.put("milkQualityTypeCode", milkQualityType);
                ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class, uriVariables);
                if (response == null || response.getStatusCode() != HttpStatus.OK)
                    return null;

                listStr = Arrays.asList(response.getBody());
            } else {
                RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY_MILK_PURCHASE_RATE + "/view/{code}/{milkTypeCode}/{milkQualityTypeCode}";
                Map<String, Object> uriVariables = new HashMap<>();
                uriVariables.put("code", code);
                uriVariables.put("milkTypeCode", milkType);
                uriVariables.put("milkQualityTypeCode", milkQualityType);
                ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class, uriVariables);
                if (response == null || response.getStatusCode() != HttpStatus.OK)
                    return null;

                listStr = Arrays.asList(response.getBody());
            }

            return listStr;
        } catch (Exception e) {
            LOGGER.error("Rate Fetch fetch", e);
        }
        return null;
    }

}
