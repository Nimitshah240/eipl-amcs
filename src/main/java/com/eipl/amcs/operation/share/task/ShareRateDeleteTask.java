package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.ShareRate;
import com.eipl.amcs.operation.share.service.ShareRateService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.Optional;

public class ShareRateDeleteTask extends Task<Boolean> {
    private final String code;

    public ShareRateDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ShareRateService service = EmcsAppContext.getContext().getBean(ShareRateService.class);
            Optional<ShareRate> shareRate = service.findById(code);
            if (shareRate == null || !shareRate.isPresent())
                return null;
            service.delete(shareRate.get(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
