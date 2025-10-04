package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class TaxLoadTask extends Task<List<TaxDto>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaxLoadTask.class);

    @Override
    protected List<TaxDto> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.TAX;
            ResponseEntity<TaxDto[]> response = restTemplate.getForEntity(url, com.eipl.amcs.master.account.dto.TaxDto[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Tax fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Tax fetch", e);
        }
        return null;
    }
}

