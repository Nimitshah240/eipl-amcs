package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.service.BillHeadService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class BillHeadDeleteTask extends Task<Boolean> {
    private final String code;

    public BillHeadDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            BillHeadService service = EmcsAppContext.getContext().getBean(BillHeadService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
