package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.administartion.dto.StaffSalaryMapping;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class StaffSalaryMappingLoadTask extends Task<List<StaffSalaryMapping>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffSalaryMappingLoadTask.class);

    @Override
    protected List<StaffSalaryMapping> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STAFF_SALARY_MAPPING;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("society", MainApp.identityDto.getSociety().getCode());

            ResponseEntity<StaffSalaryMapping[]> response = restTemplate.getForEntity(url, StaffSalaryMapping[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("StaffSalaryMapping fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("StaffSalaryMapping fetch", e);
        }
        return null;
    }
}
