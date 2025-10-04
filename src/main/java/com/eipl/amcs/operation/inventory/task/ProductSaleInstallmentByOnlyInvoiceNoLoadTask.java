package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductSaleInstallmentByOnlyInvoiceNoLoadTask extends Task<List<ProductSaleInstallment>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleInstallmentByOnlyInvoiceNoLoadTask.class);

    private final String code;


    public ProductSaleInstallmentByOnlyInvoiceNoLoadTask(String code) {
        this.code = code;

    }

    @Override
    protected List<ProductSaleInstallment> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_SALE_TO_MEMBER_INSTALLMENT + "/invoiceNo";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("invoiceNo",code);
            ResponseEntity<ProductSaleInstallment[]> response  = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, ProductSaleInstallment[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductSaleToMemberInstallment fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("ProductSaleToMemberInstallment fetch", e);
        }
        return null;
    }
}
