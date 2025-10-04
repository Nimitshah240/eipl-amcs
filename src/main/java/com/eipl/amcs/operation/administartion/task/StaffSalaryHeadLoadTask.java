package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.administartion.dto.StaffSalaryHead;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class StaffSalaryHeadLoadTask extends Task<List<StaffSalaryHead>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffSalaryHeadLoadTask.class);

    @Override
    protected List<StaffSalaryHead> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STAFF_SALARY_HEAD;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("society", MainApp.identityDto.getSociety().getCode());

            ResponseEntity<StaffSalaryHead[]> response = restTemplate.getForEntity(url, StaffSalaryHead[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("StaffSalaryHead fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("StaffSalaryHead fetch", e);
        }
        return null;
    }
}
