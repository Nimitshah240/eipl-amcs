package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.administartion.dto.CashAdvance;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class CashAdvanceLoadTask extends Task<List<CashAdvance>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAdvanceLoadTask.class);

    @Override
    protected List<CashAdvance> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.CASHADVANCE;
            ResponseEntity<CashAdvance[]> response = restTemplate.getForEntity(url, CashAdvance[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("CashAdvance fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("CashAdvance fetch", e);
        }
        return null;
    }
}
