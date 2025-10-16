package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.dto.ProductSaleMigrateDto;
import com.eipl.amcs.operation.inventory.service.ProductSaleService;
import com.eipl.amcs.util.CommonUtil;
import javafx.concurrent.Task;

import java.util.List;

public class ProductSaleMigrationListSaveTask extends Task<Integer> {
    private final List<ProductSaleMigrateDto> dtoList;

    private final int max;

    public ProductSaleMigrationListSaveTask(List<ProductSaleMigrateDto> dtoList, int max) {
        this.dtoList = dtoList;
        this.max = max;

    }

    @Override
    protected Integer call() throws Exception {
        try {
            ProductSaleService service = EmcsAppContext.getContext().getBean(ProductSaleService.class);
            service.migrateCollections(dtoList, CommonUtil.setIdentityHeader());

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_SALE + "/migrate";
//            ResponseEntity<ProductSaleMigrateDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), ProductSaleMigrateDto[].class);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
