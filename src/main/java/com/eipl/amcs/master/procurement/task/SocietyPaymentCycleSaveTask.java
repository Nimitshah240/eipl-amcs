package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
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
import java.util.List;
import java.util.Map;

public class SocietyPaymentCycleSaveTask extends Task<Object> {
    private final List<SocietyPaymentCycle> paymentCycleList;
    private final short update;

    public SocietyPaymentCycleSaveTask(List<SocietyPaymentCycle> paymentCycleList, short update) {
        this.paymentCycleList = paymentCycleList;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY_PAYMENT_CYCLE;

            ResponseEntity<Object> response = null;
            if (this.update == 0) {
                response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(paymentCycleList), Object.class);
                if (response == null || response.getStatusCode() != HttpStatus.CREATED)
                    return null;
                return response.getBody().toString();
            } else {
                url = url + "/{code}";
                Map<String, Object> uriVariables = new HashMap<>();
                uriVariables.put("code", paymentCycleList.get(0).getCode());
                response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(paymentCycleList.get(0)), Object.class);
                if (response == null || response.getStatusCode() != HttpStatus.OK)
                    return null;
                return response.getBody();
            }
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
