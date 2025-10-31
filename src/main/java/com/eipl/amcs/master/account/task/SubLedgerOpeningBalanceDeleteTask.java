package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.SubLedgerOpeningBalanceService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class SubLedgerOpeningBalanceDeleteTask extends Task<Boolean> {
    private final String code;

    public SubLedgerOpeningBalanceDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            SubLedgerOpeningBalanceService service = EmcsAppContext.getContext().getBean(SubLedgerOpeningBalanceService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
