package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.StaffSalaryHead;
import com.eipl.amcs.master.account.service.StaffSalaryHeadService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StaffSalaryHeadLoadTask extends Task<List<StaffSalaryHead>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffSalaryHeadLoadTask.class);

    @Override
    protected List<StaffSalaryHead> call() throws Exception {
        try {
            StaffSalaryHeadService service = EmcsAppContext.getContext().getBean(StaffSalaryHeadService.class);
            List<StaffSalaryHead> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("StaffSalaryHead fetch", e);
        }
        return null;
    }
}
