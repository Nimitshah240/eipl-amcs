package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.dto.ProductGroup;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ProductGroupByIdLoadTask extends Task<ProductGroup> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductGroupByIdLoadTask.class);

    private final String code;

    public ProductGroupByIdLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected ProductGroup call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_GROUP + "/{code}";
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("code", code);
            ResponseEntity<ProductGroup> response = restTemplate.exchange(url, HttpMethod.GET, null, ProductGroup.class, uriVariables);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductGroupById fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("ProductGroupById fetch", e);
        }
        return null;
    }
}
