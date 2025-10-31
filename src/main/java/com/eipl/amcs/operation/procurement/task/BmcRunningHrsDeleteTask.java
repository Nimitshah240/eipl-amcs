package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.service.BmcRunningHoursService;
import javafx.concurrent.Task;

public class BmcRunningHrsDeleteTask extends Task<Boolean> {
    private final Long code;

    public BmcRunningHrsDeleteTask(Long code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            BmcRunningHoursService service = EmcsAppContext.getContext().getBean(BmcRunningHoursService.class);
            service.deleteBmcRunningHours(code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
