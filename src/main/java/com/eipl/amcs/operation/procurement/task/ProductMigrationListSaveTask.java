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
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
