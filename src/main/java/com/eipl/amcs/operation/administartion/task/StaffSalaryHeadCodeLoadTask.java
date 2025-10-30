package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaffSalaryHeadCodeLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffSalaryHeadCodeLoadTask.class);
    private final String society;

    public StaffSalaryHeadCodeLoadTask(String society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String code = nextCodeService.getNextCode("StaffSalaryHead", "code", society, 2);
            if (code == null || code.isBlank())
                return null;
            return code;
        } catch (Exception e) {
            LOGGER.error("Staff Salary Head fetch", e);
        }
        return null;
    }
}
