package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.administartion.dto.StaffSalaryMapping;
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

public class StaffSalaryMappingSaveTask extends Task<Object> {
    private List<StaffSalaryMapping> paymentCycleList;
    private short update;

    public StaffSalaryMappingSaveTask(List<StaffSalaryMapping> paymentCycleList, short update) {
        this.paymentCycleList = paymentCycleList;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STAFF_SALARY_MAPPING;

            ResponseEntity<Object> response = null;
            if (this.update == 0) {
                response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(paymentCycleList), Object.class);
                if (response == null || response.getStatusCode() != HttpStatus.CREATED)
                    return null;
                return response.getBody().toString();
            } else {
//                url = url + "/{code}";
//                Map<String, Object> uriVariables = new HashMap<>();
//                uriVariables.put("code", paymentCycleList.get(0).getCode());
//                response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(paymentCycleList.get(0)), Object.class);
//                if (response == null || response.getStatusCode() != HttpStatus.OK)
//                    return null;
//                return response.getBody();
            }
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
