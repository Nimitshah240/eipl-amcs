package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class ProductRequisitionTransactionsByProductRequisitionCodeLoadTask extends Task<ProductRequisitionDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.eipl.amcs.operation.inventory.task.ProductRequisitionTransactionsByProductRequisitionCodeLoadTask.class);

    private final String code;
    public ProductRequisitionTransactionsByProductRequisitionCodeLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected ProductRequisitionDto call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) +
                    AppConstant.UrlPath.PRODUCT_REQUISITION_TRANSACTION+ "/ByGrnNo";
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("code", code);
            ResponseEntity<ProductRequisitionDto> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                    HttpMethod.GET, null, ProductRequisitionDto.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductReceiptTransactions fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("ProductReceiptTransactions fetch", e);
        }
        return null;
    }
}
