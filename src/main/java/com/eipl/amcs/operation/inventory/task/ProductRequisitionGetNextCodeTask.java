package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ProductRequisitionGetNextCodeTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRequisitionGetNextCodeTask.class);

    public ProductRequisitionGetNextCodeTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            String code = MainApp.identityDto.getSociety().getCode() + "/" + LocalDate.now() + "/";
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            return nextCodeService.getNextCode("ProductRequisition", "code", code, 0);
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return null;
    }

}
