package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ProductPurchaseRateLoadTask extends Task<List<ProductPurchaseRate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPurchaseRateLoadTask.class);

    @Override
    protected List<ProductPurchaseRate> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_PURCHASE_RATE;
            ResponseEntity<ProductPurchaseRate[]> response = restTemplate.getForEntity(url, ProductPurchaseRate[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductPurchaseRate fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("ProductPurchaseRate fetch", e);
        }
        return null;
    }
}
