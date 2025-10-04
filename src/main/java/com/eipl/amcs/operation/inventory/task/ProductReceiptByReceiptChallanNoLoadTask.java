package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
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

public class ProductReceiptByReceiptChallanNoLoadTask extends Task<ProductReceipt> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptByReceiptChallanNoLoadTask.class);
    private final String grnNo;
    private final String challanNo;

    public ProductReceiptByReceiptChallanNoLoadTask(String receiptNo, String challanNo) {
        this.grnNo = receiptNo;
        this.challanNo = challanNo;
    }

    @Override
    protected ProductReceipt call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_RECEIPT_MATERIAL + "/{grnNo}" + "/{challanNo}";
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("grnNo", grnNo);
            uriVariables.put("challanNo", challanNo);
            ResponseEntity<ProductReceipt> response = restTemplate.exchange(url, HttpMethod.GET, null, ProductReceipt.class, uriVariables);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductReceipt fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }
}
