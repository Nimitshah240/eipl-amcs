package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ProductReceiptLoadTask extends Task<List<ProductReceipt>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptLoadTask.class);

    private LocalDate fromDate, toDate;

    public ProductReceiptLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<ProductReceipt> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_RECEIPT_MATERIAL;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString());
            ResponseEntity<ProductReceipt[]> response = restTemplate.getForEntity(builder.toUriString(), ProductReceipt[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
