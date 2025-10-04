package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ProductSalenstallmentLoadTask extends Task<List<ProductSaleInstallment>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSalenstallmentLoadTask.class);

    @Override
    protected List<ProductSaleInstallment> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_SALE_TO_MEMBER_INSTALLMENT;
            ResponseEntity<ProductSaleInstallment[]> response = restTemplate.getForEntity(url, ProductSaleInstallment[].class);
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
