package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateApplicability;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class SocietyMilkPurchaseRateApplicabilityLoadTask extends Task<List<SocietyMilkPurchaseRateApplicability>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyMilkPurchaseRateApplicabilityLoadTask.class);

    @Override
    protected List<SocietyMilkPurchaseRateApplicability> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY_MILK_PURCHASE_RATE_APPLICABILITY;
            ResponseEntity<SocietyMilkPurchaseRateApplicability[]> response = restTemplate.getForEntity(url, SocietyMilkPurchaseRateApplicability[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("SocietyMilkPurchaseRateApplicability fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("SocietyMilkPurchaseRateApplicability fetch", e);
        }
        return null;
    }
}

