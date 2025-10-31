package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.LedgerOpeningBalanceService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class LedgerOpeningBalanceDeleteTask extends Task<Boolean> {
    private final String code;

    public LedgerOpeningBalanceDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            LedgerOpeningBalanceService service = EmcsAppContext.getContext().getBean(LedgerOpeningBalanceService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
