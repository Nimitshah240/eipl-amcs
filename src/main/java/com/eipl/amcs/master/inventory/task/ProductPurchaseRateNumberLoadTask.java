package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class ProductPurchaseRateNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPurchaseRateLoadTask.class);
    private final String productPurchaseRate;

    public ProductPurchaseRateNumberLoadTask(String productPurchaseRate) {
        this.productPurchaseRate = productPurchaseRate;
    }

    @Override
    protected String call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_PURCHASE_RATE_NUMBER;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("productPurchaseRate", productPurchaseRate);

            ResponseEntity<String> response = restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, null, String.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("productPurchaseRate Number fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("productPurchaseRate Number fetch", e);
        }
        return null;
    }
}
