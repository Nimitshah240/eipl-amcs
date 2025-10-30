package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.List;

public class LocalMilkSaleMigrationListSaveTask extends Task<Integer> {
    private final List<LocalMilkSale> dtoList;
    private final int max;

    public LocalMilkSaleMigrationListSaveTask(List<LocalMilkSale> dtoList, int max) {
        this.dtoList = dtoList;
        this.max = max;
    }

    @Override
    protected Integer call() throws Exception {
        try {
            LocalMilkSaleService service = EmcsAppContext.getContext().getBean(LocalMilkSaleService.class);
            service.migrateCollections(dtoList, CommonUtils.setIdentityHeader());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
