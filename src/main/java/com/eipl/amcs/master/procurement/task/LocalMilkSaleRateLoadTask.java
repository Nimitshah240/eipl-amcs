package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.service.LocalMilkSaleRateService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class LocalMilkSaleRateLoadTask extends Task<List<LocalMilkSaleRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleRateLoadTask.class);

    @Override
    protected List<LocalMilkSaleRate> call() throws Exception {
        try {
            LocalMilkSaleRateService service= EmcsAppContext.getContext().getBean(LocalMilkSaleRateService.class);
            List<LocalMilkSaleRate> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LOCAL_MILK_SALE_RATE;
//            ResponseEntity<LocalMilkSaleRate[]> response = restTemplate.getForEntity(url, LocalMilkSaleRate[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("LocalMilkSaleRates fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("LocalMilkSaleRates fetch", e);
        }
        return null;
    }
}

