package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductLoadTask.class);
    private final String society;

    public ProductNumberLoadTask(String society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            return nextCodeService.getNextCode("Product", "code", society, 0);
        } catch (Exception e) {
            LOGGER.error("product Number fetch", e);
        }
        return null;
    }
}
