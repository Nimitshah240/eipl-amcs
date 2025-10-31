package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.StaffSalaryHeadService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class StaffSalaryHeadDeleteTask extends Task<Boolean> {
    private final Integer code;

    public StaffSalaryHeadDeleteTask(Integer code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            StaffSalaryHeadService service = EmcsAppContext.getContext().getBean(StaffSalaryHeadService.class);
            service.delete(code.toString(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
