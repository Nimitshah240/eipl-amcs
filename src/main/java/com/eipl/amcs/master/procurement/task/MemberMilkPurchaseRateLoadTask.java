package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class MemberMilkPurchaseRateLoadTask extends Task<List<MemberMilkPurchaseRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberMilkPurchaseRateLoadTask.class);

    @Override
    protected List<MemberMilkPurchaseRate> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_MILK_PURCHASE_RATE;
            ResponseEntity<MemberMilkPurchaseRate[]> response = restTemplate.getForEntity(url, MemberMilkPurchaseRate[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("MemberMilkPurchaseRates fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("MemberMilkPurchaseRates fetch", e);
        }
        return null;
    }
}

