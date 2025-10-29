package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SocietyMilkPurchaseRateLoadTask extends Task<List<SocietyMilkPurchaseRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyMilkPurchaseRateLoadTask.class);

    @Override
    protected List<SocietyMilkPurchaseRate> call() throws Exception {
        try {
            SocietyMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);
            List<SocietyMilkPurchaseRate> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY_MILK_PURCHASE_RATE;
//            ResponseEntity<SocietyMilkPurchaseRate[]> response = restTemplate.getForEntity(url, SocietyMilkPurchaseRate[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("SocietyMilkPurchaseRates fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("SocietyMilkPurchaseRates fetch", e);
        }
        return null;
    }
}

