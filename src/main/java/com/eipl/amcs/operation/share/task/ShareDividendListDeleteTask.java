package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.service.ShareDividendService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class ShareDividendListDeleteTask extends Task<Boolean> {
    private final String code;

    public ShareDividendListDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ShareDividendService service = EmcsAppContext.getContext().getBean(ShareDividendService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}