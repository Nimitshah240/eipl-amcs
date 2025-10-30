package com.eipl.amcs.master.account.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LedgerGroupNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerTypeLoadTask.class);
    private final String society;

    public LedgerGroupNumberLoadTask(String society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String code = nextCodeService.getNextCode("LedgerGroup", "code", society, 0);
            if (code == null || code.isEmpty())
                return null;
            return code;
        } catch (Exception e) {
            LOGGER.error("Ledger Group fetch", e);
        }
        return null;
    }
}
