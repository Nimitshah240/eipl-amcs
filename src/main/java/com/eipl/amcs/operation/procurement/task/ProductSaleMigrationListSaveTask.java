package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.dto.ProductSaleDto;
import com.eipl.amcs.operation.inventory.dto.ProductSaleMigrateDto;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ProductSaleMigrationListSaveTask extends Task<Integer> {
    private final List<ProductSaleMigrateDto> dtoList;

    private int max;

    public ProductSaleMigrationListSaveTask(List<ProductSaleMigrateDto> dtoList, int max) {
        this.dtoList = dtoList;
        this.max = max;

    }

    @Override
    protected Integer call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_SALE + "/migrate";
            ResponseEntity<ProductSaleMigrateDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), ProductSaleMigrateDto[].class);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
