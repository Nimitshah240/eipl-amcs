package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberCodeLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberCodeLoadTask.class);
    private final String society;

    public MemberCodeLoadTask(String society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String code = nextCodeService.getNextCode("Member", "code", society, 4);
            if (code == null || code.isEmpty())
                return null;
            return code;
        } catch (Exception e) {
            LOGGER.error("Member Number fetch", e);
        }
        return null;
    }
}
