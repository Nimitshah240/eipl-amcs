package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductAndSaleRateDto;
import com.eipl.amcs.master.inventory.service.ProductService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.List;

public class ProductMigrationListSaveTask extends Task<Integer> {
    private final List<ProductAndSaleRateDto> dtoList;
    private final int max;

    public ProductMigrationListSaveTask(List<ProductAndSaleRateDto> dtoList, int max) {
        this.dtoList = dtoList;
        this.max = max;
    }

    @Override
    protected Integer call() throws Exception {
        try {
            ProductService service = EmcsAppContext.getContext().getBean(ProductService.class);
            service.migrateCollections(dtoList, CommonUtils.setIdentityHeader());

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT + "/migrate";
//            ResponseEntity<ProductAndSaleRateDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), ProductAndSaleRateDto[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
