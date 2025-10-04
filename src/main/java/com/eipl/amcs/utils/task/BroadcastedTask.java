package com.eipl.amcs.utils.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BroadcastedTask extends Task<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastedTask.class);

    @Override
    protected Void call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.IDENTITY_CALL + "/sendBroadcastedAllInOne";
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            if (response.getStatusCode() != HttpStatus.OK)
                return null;
        } catch (Exception e) {
            LOGGER.error("Broadcast Data All In One", e);
        }
        return null;
    }
}

