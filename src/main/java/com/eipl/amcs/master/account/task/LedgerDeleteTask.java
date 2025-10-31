package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class LedgerDeleteTask extends Task<Boolean> {
    private final String code;

    public LedgerDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            LedgerService service = EmcsAppContext.getContext().getBean(LedgerService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
