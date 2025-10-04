package com.eipl.amcs.master.global.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class GenderLoadTask extends Task<List<Gender>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenderLoadTask.class);

    @Override
    protected List<com.eipl.amcs.master.global.model.Gender> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.GENDER;
            ResponseEntity<com.eipl.amcs.master.global.model.Gender[]> response = restTemplate.getForEntity(url, com.eipl.amcs.master.global.model.Gender[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Genders fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Genders fetch", e);
        }
        return null;
    }
}

