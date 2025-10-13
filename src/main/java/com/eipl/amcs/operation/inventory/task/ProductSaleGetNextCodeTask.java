package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductSaleGetNextCodeTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleGetNextCodeTask.class);

    public ProductSaleGetNextCodeTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            String code = MainApp.identityDto.getSociety().getCode() + "/" + MainApp.getFinancialYear().getCode() + "/";

            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            return nextCodeService.getNextCode("ProductSale", "invoiceNo", code, 6);
        } catch (Exception e) {
            LOGGER.error("ProductSale No fetch", e);
        }
        return null;
    }

}
