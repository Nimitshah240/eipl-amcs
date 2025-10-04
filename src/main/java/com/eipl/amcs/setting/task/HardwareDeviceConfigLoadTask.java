package com.eipl.amcs.setting.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class HardwareDeviceConfigLoadTask extends Task<List<HardwareDeviceConfig>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareDeviceConfigLoadTask.class);

    @Override
    protected List<HardwareDeviceConfig> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.HARDWARE_DEVICE_CONFIG;

            ResponseEntity<HardwareDeviceConfig[]> response = restTemplate.getForEntity(url, HardwareDeviceConfig[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("HardwareDeviceConfigs fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("HardwareDeviceConfigs fetch", e);
        }
        return null;
    }
}

