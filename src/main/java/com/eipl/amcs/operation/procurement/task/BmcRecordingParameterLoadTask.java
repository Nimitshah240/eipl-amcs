package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.RecordingParameter;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class BmcRecordingParameterLoadTask extends Task<List<RecordingParameter>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BmcRecordingParameterLoadTask.class);

    @Override
    protected List<RecordingParameter> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.RECORDING_PARAMETER;
            ResponseEntity<RecordingParameter[]> response = restTemplate.getForEntity(url, RecordingParameter[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("RecordingParameter fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("RecordingParameter fetch", e);
        }
        return null;
    }
}
