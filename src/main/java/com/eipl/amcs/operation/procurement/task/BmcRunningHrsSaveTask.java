package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.BmcRunningHrs;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class BmcRunningHrsSaveTask extends Task<Object> {
    private final BmcRunningHrs dto;
    private final short update;

    public BmcRunningHrsSaveTask(BmcRunningHrs dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            if (dto == null) {
                // Handle the case when dto is null
                return false;
            }

            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url;
            ResponseEntity<BmcRunningHrs> response;

            if (this.update == 0) {
                url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BMC_RUNNING_HRS;
                // POST request for creating a new record
                response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), BmcRunningHrs.class);
            } else {
                url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BMC_RUNNING_HRS;
                // PUT request for updating an existing record
                Map<String, Object> uriVariables = new HashMap<>();
                Long code = dto.getCode();
                if (code == null) {
                    // Handle the case when code is null
                    return false;
                }
                uriVariables.put("code", code);
                response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), BmcRunningHrs.class, uriVariables);
            }

            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                // Return true if the API call was successful
                return true;
            }
        } catch (HttpStatusCodeException e) {
            // Handle exceptions from the API call and parse the error response
            String errorResponse = e.getResponseBodyAsString();
            EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return false if there was an issue with the API call
        return false;
    }
}