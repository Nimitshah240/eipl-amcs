package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.VoucherTypeService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class VoucherTypeDeleteTask extends Task<Boolean> {
    private final String code;

    public VoucherTypeDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            VoucherTypeService service = EmcsAppContext.getContext().getBean(VoucherTypeService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
