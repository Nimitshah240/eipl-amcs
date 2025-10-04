package com.eipl.amcs.master.global.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class MilkQualityTypeLoadTask extends Task<List<MilkQualityType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkQualityTypeLoadTask.class);

    @Override
    protected List<MilkQualityType> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILKQUALITYTYPE;
            ResponseEntity<MilkQualityType[]> response = restTemplate.getForEntity(url, MilkQualityType[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("MilkQualityTypes fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("MilkQualityTypes fetch", e);
        }
        return null;
    }
}

