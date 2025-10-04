package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.dto.InsuranceDetail;
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

public class InsuranceDetailLoadTask extends Task<List<InsuranceDetail>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceDetailLoadTask.class);
    private final Integer insuranceMasterCode;

    public InsuranceDetailLoadTask(Integer insuranceMasterCode) {
        this.insuranceMasterCode = insuranceMasterCode;
    }

    @Override
    protected List<InsuranceDetail> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.INSURANCE + "/detail";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("insuranceMasterCode", insuranceMasterCode);

            ResponseEntity<InsuranceDetail[]> response = restTemplate.getForEntity(builder.toUriString(), InsuranceDetail[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("InsuranceDetail fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("InsuranceDetail fetch", e);
        }
        return null;
    }
}
