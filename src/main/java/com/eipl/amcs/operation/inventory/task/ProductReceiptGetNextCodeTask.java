package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductReceiptGetNextCodeTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptGetNextCodeTask.class);

    public ProductReceiptGetNextCodeTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            String code = MainApp.identityDto.getSociety().getCode() + "/" + MainApp.getFinancialYear().getCode() + "/";
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            return nextCodeService.getNextCode("ProductReceipt", "grnNo", code, 0);
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }

}
