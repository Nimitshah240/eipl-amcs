package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.service.BranchService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BranchLoadTask extends Task<List<Branch>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BranchLoadTask.class);
    private Bank bank;

    public BranchLoadTask() {

    }

    public BranchLoadTask(Bank bank) {
        this.bank = bank;
    }

    @Override
    protected List<Branch> call() throws Exception {
        try {
            BranchService service = EmcsAppContext.getContext().getBean(BranchService.class);
            List<Branch> list = null;
            if (bank == null || bank.getCode() == null || bank.getCode().isEmpty())
                list = service.findAll();
            else
                list = service.findAll(bank.getCode());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Branch fetch", e);
        }
        return null;
    }
}
