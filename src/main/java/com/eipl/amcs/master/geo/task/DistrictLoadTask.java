package com.eipl.amcs.master.geo.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class DistrictLoadTask extends Task<List<District>> {
    private com.eipl.amcs.master.geo.model.State state;
    private static final Logger LOGGER = LoggerFactory.getLogger(DistrictLoadTask.class);

    public DistrictLoadTask() {
    }

    public DistrictLoadTask(com.eipl.amcs.master.geo.model.State state) {
        this.state = state;
    }

    @Override
    protected List<District> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.DISTRICT;
            ResponseEntity<District[]> response = null;

            if (state != null) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                        .queryParam("stateCode", state.getCode());
                response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, District[].class);
            } else {
                response = restTemplate.getForEntity(url, District[].class);
            }
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Districts fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Districts fetch", e);
        }
        return null;
    }
}

