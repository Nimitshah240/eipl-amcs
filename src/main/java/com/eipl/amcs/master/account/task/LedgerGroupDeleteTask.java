package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.LedgerGroupService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class LedgerGroupDeleteTask extends Task<Boolean> {
    private final String code;

    public LedgerGroupDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            LedgerGroupService service = EmcsAppContext.getContext().getBean(LedgerGroupService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
