package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class SubLedgerDeleteTask extends Task<Boolean> {
    private final String code;

    public SubLedgerDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            SubLedgerService service = EmcsAppContext.getContext().getBean(SubLedgerService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
