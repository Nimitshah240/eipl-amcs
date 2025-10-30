package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaffCodeLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffCodeLoadTask.class);
    private final String society;

    public StaffCodeLoadTask(String society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        try {

            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String code = nextCodeService.getNextCode("StaffMember", "code", society, 0);
            if (code == null || code.isBlank())
                return null;
            return code;

        } catch (Exception e) {
            LOGGER.error("Staff Number fetch", e);
        }
        return null;
    }
}
