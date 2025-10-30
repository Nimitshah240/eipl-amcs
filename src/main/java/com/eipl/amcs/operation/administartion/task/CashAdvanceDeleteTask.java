package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.CashAdvanceService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class CashAdvanceDeleteTask extends Task<Boolean> {
    private final String code;

    public CashAdvanceDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            CashAdvanceService service = EmcsAppContext.getContext().getBean(CashAdvanceService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
