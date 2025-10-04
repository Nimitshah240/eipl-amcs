package com.eipl.amcs.operation.inventory.task;


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

public class ProductReceiptDeleteTask extends Task<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.eipl.amcs.master.inventory.task.ProductSaleRateByProductLoadTask.class);

    private final String grnNo;

    public ProductReceiptDeleteTask(String grnNo) {
        this.grnNo = grnNo;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_RECEIPT_MATERIAL + "/delete";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("grnNo", grnNo);
            ResponseEntity<Void> response = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, Void.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
