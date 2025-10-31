package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShareGetNextCodeTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareGetNextCodeTask.class);

    public ShareGetNextCodeTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String codes = nextCodeService.getNextCode("Share", "code", MainApp.identityDto.getSociety().getCode(), 5);
            if (codes == null || codes.isEmpty()) return null;
            return codes;
        } catch (Exception e) {
            LOGGER.error("Share No fetch", e);
        }
        return null;
    }

}
