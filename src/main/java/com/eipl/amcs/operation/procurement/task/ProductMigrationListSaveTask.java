package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.operation.procurement.dto.ProductAndSaleRateDto;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ProductMigrationListSaveTask extends Task<Integer> {
    private final List<ProductAndSaleRateDto> dtoList;
    private int max;

    public ProductMigrationListSaveTask(List<ProductAndSaleRateDto> dtoList, int max) {
        this.dtoList = dtoList;
        this.max = max;
    }

    @Override
    protected Integer call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT + "/migrate";
            ResponseEntity<ProductAndSaleRateDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), ProductAndSaleRateDto[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
