package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductSaleTax;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ProductSaleTaxLoadTask extends Task<List<ProductSaleTax>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleTaxLoadTask.class);

    @Override
    protected List<ProductSaleTax> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_SALE_TO_MEMBER_TAX_CALCULATED;
            ResponseEntity<ProductSaleTax[]> response = restTemplate.getForEntity(url, ProductSaleTax[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductSaleToMemberTaxCalculated fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("ProductSaleToMemberTaxCalculated fetch", e);
        }
        return null;
    }
}
