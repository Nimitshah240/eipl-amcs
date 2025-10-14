package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.service.BankService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BankLoadTask extends Task<List<Bank>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankLoadTask.class);

    @Override
    protected List<Bank> call() throws Exception {
        try {
            BankService service = EmcsAppContext.getContext().getBean(BankService.class);
            List<Bank> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Bank fetch", e);
        }
        return null;
    }
}
