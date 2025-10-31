package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.service.BillCriteriaService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class BillCriteriaDeleteTask extends Task<Boolean> {
    private final String code;

    public BillCriteriaDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            BillCriteriaService service = EmcsAppContext.getContext().getBean(BillCriteriaService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
