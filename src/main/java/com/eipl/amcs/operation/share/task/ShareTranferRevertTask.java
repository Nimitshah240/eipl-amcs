package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.service.ShareService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class ShareTranferRevertTask extends Task<Boolean> {
    private final String code;

    public ShareTranferRevertTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ShareService service = EmcsAppContext.getContext().getBean(ShareService.class);
            service.transferRevert(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}