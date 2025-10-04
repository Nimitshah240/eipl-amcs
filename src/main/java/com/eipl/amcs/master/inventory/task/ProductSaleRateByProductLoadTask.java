package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProductSaleRateByProductLoadTask extends Task<ProductSaleRate> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleRateByProductLoadTask.class);

    private final String code;
    private final LocalDate date;

    public ProductSaleRateByProductLoadTask(String code, LocalDate date) {
        this.code = code;
        this.date = date;
    }

    @Override
    protected ProductSaleRate call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_SALE_RATE + "/findRate";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("code", code)
                    .queryParam("date", date.toString());
            ResponseEntity<ProductSaleRate> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, ProductSaleRate.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductSaleRate fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("ProductSaleRate fetch", e);
        }
        return null;
    }
}
