package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class HardwareDeviceLoadTask extends Task<List<HardwareDevice>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareDeviceLoadTask.class);

    @Override
    protected List<HardwareDevice> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.HARDWAREDEVICE;
            ResponseEntity<HardwareDevice[]> response = restTemplate.getForEntity(url, HardwareDevice[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("HardwareDevices fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("HardwareDevices fetch", e);
        }
        return null;
    }
}

