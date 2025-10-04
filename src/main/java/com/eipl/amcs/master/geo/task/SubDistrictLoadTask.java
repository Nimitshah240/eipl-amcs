package com.eipl.amcs.master.geo.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.SubDistrict;
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

public class SubDistrictLoadTask extends Task<List<SubDistrict>> {
    private District district;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubDistrictLoadTask.class);

    public SubDistrictLoadTask() {
    }

    public SubDistrictLoadTask(District district) {
        this.district = district;
    }

    @Override
    protected List<com.eipl.amcs.master.geo.model.SubDistrict> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUBDISTRICT;
            ResponseEntity<SubDistrict[]> response = null;
            if (district != null) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                        .queryParam("districtCode", district.getCode());
                response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, SubDistrict[].class);
            } else {
                response = restTemplate.getForEntity(url, SubDistrict[].class);
            }

            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("SubDistricts fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("SubDistricts fetch", e);
        }
        return null;
    }
}

