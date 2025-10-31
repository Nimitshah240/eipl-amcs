package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.service.LedgerTypeService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LedgerTypeLoadTask extends Task<List<LedgerType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerTypeLoadTask.class);

    @Override
    protected List<LedgerType> call() throws Exception {
        try {
            LedgerTypeService service = EmcsAppContext.getContext().getBean(LedgerTypeService.class);
            List<LedgerType> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("LedgerType fetch", e);
        }
        return null;
    }
}
