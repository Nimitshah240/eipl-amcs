package com.eipl.amcs.base.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.dto.JarUpdate;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class UpdaterCheckTask extends Task<JarUpdate> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdaterCheckTask.class);

    @Override
    protected JarUpdate call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty("syncUrl.realtime", AppConstant.UrlPath.DATA_PROCESSOR) + AppConstant.UrlPath.APP_UPDATE;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("client_code", MainApp.getProperty("client.code", AppConstant.UrlPath.JAIPUR));

            ResponseEntity<JarUpdate> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST,
                    new HttpEntity<>(new JarUpdate(MainApp.getProperty("identity.version", AppConstant.versionNo), MainApp.identityDto.getSociety().getCode())), JarUpdate.class);
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