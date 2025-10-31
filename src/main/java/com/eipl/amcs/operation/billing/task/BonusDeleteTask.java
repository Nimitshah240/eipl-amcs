package com.eipl.amcs.operation.billing.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.service.BonusService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;


public class BonusDeleteTask extends Task<Boolean> {
    private final String code;

    public BonusDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);
            service.deleteDto(CommonUtils.setIdentityHeader(), code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
