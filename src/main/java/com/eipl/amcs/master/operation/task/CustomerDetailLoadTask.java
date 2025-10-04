package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class CustomerDetailLoadTask extends Task<CustomerDetails> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDetailLoadTask.class);

    private final String code;

    public CustomerDetailLoadTask(String code) {
        this.code = code;
    }
    @Override
    protected CustomerDetails call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.CUSTOMER_DETAIL + "/{code}";
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("code", code);
            ResponseEntity<CustomerDetails> response = restTemplate.exchange(url, HttpMethod.GET, null, CustomerDetails.class, uriVariables);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Customer fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Customer fetch", e);
        }
        return null;
    }
}