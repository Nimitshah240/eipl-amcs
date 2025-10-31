package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class SocietyPaymentCycleDeleteTask extends Task<Boolean> {
    private final String code;

    public SocietyPaymentCycleDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            SocietyPaymentCycleService service = EmcsAppContext.getContext().getBean(SocietyPaymentCycleService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
