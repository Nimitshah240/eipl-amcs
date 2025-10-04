package com.eipl.amcs.operation.inventory.task;


import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ProductReceiptTransactionsByGrnNoLoadTask extends Task<List<ReceiptTxnTaxDto>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask.class);

    private final String code;
    public ProductReceiptTransactionsByGrnNoLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<ReceiptTxnTaxDto> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) +
                    AppConstant.UrlPath.PRODUCT_RECEIPT_TRANSACTION+ "/ByGrnNo";
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("code", code);
            ResponseEntity<ReceiptTxnTaxDto[]> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),
                    HttpMethod.GET, null, ReceiptTxnTaxDto[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductReceiptTransactions fetched: {}", response.getBody());
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("ProductReceiptTransactions fetch", e);
        }
        return null;
    }
}
