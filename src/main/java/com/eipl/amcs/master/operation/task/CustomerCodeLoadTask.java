package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerCodeLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCodeLoadTask.class);
    private final String society;

    public CustomerCodeLoadTask(String society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String code = nextCodeService.getNextCode("Customer", "code", society, 4);
            if (code == null || code.isEmpty())
                return null;
            return code;
        } catch (Exception e) {
            LOGGER.error("Customer Number fetch", e);
        }
        return null;
    }
}
