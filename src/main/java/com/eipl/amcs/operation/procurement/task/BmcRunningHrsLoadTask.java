package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.BmcRunningHours;
import com.eipl.amcs.operation.procurement.service.BmcRunningHoursService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BmcRunningHrsLoadTask extends Task<List<BmcRunningHours>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BmcRunningHrsLoadTask.class);

    @Override
    protected List<BmcRunningHours> call() throws Exception {
        try {
            BmcRunningHoursService service = EmcsAppContext.getContext().getBean(BmcRunningHoursService.class);
            List<BmcRunningHours> list = service.getAllBmcRunningHours();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("BmcRunningHrs fetch", e);
        }
        return null;
    }
}
