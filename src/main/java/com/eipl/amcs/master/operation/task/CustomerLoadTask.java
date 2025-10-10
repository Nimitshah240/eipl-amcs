package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class CustomerLoadTask extends Task<List<Customer>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerLoadTask.class);

    @Override
    protected List<Customer> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.CUSTOMER;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("society", MainApp.identityDto.getSociety().getCode());

            ResponseEntity<Customer[]> response = restTemplate.getForEntity(builder.toUriString(), Customer[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Customer fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Customer fetch", e);
        }
        return null;
    }
}