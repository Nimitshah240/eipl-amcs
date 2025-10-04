package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTax;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ProductReceiptTaxLoadTask extends Task<List<ProductReceiptTax>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptTaxLoadTask.class);

    @Override
    protected List<ProductReceiptTax> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_RECEIPT_TAX_CALCULATED;
            ResponseEntity<ProductReceiptTax[]> response = restTemplate.getForEntity(url, ProductReceiptTax[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductReceiptTax fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("ProductReceiptTax fetch", e);
        }
        return null;
    }
}
