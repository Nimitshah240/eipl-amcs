package com.eipl.amcs.master.geo.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.*;
//import com.eipl.amcs.master.geo.dto.Village;
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

public class HamletLoadTask extends Task<List<Hamlet>> {
    private Village village;
    private static final Logger LOGGER = LoggerFactory.getLogger(HamletLoadTask.class);

    public HamletLoadTask() {

    }

    public HamletLoadTask(Village village) {
        this.village = village;
    }

    @Override
    protected List<com.eipl.amcs.master.geo.model.Hamlet> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.HAMLET;
            ResponseEntity<Hamlet[]> response;
            if (village != null) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                        .queryParam("villageCode", village.getCode());
                response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, Hamlet[].class);
            } else {
                response = restTemplate.getForEntity(url, Hamlet[].class);
            }
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Hamlets fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Hamlets fetch", e);
        }
        return null;
    }
}

