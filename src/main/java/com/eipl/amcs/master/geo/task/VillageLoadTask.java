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

public class VillageLoadTask extends Task<List<Village>> {
    private SubDistrict subDistrict;
    private static final Logger LOGGER = LoggerFactory.getLogger(VillageLoadTask.class);

    public VillageLoadTask() {

    }

    public VillageLoadTask(SubDistrict subDistrict) {
        this.subDistrict = subDistrict;
    }

    @Override
    protected List<Village> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.VILLAGE;
            ResponseEntity<Village[]> response;
            if (subDistrict != null) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                        .queryParam("subDistrictCode", subDistrict.getCode());
                response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, Village[].class);
            } else {
                response = restTemplate.getForEntity(url, Village[].class);
            }
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Villages fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Villages fetch", e);
        }
        return null;
    }
}

