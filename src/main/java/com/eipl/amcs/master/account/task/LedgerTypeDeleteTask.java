package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.LedgerTypeService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class LedgerTypeDeleteTask extends Task<Boolean> {
    private final String code;

    public LedgerTypeDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            LedgerTypeService service = EmcsAppContext.getContext().getBean(LedgerTypeService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
