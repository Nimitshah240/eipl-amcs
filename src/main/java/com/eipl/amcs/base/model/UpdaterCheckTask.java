package com.eipl.amcs.base.model;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdaterCheckTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdaterCheckTask.class);

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = AppConstant.UrlPath.UPDATE_CHECK;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("eipl_code", AppConstant.UrlPath.AMULAMCS)
//                    .queryParam("societyCode", MainApp.identityDto.getSociety().getCode())
//                    .queryParam("version", MainApp.getProperty("identity.version","1.0"))
                    .queryParam("tab_type", 0);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Updater Information fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Updater Information Failed", e);
        }
        return null;
    }
}