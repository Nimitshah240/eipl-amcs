package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.dto.ProductSaleMigrateDto;
import com.eipl.amcs.operation.inventory.service.ProductSaleService;
import com.eipl.amcs.utils.CommonUtils;
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
            service.migrateCollections(dtoList, CommonUtils.setIdentityHeader());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
