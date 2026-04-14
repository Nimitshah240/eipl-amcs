package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.service.AccountPostingService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AccountPostingTxnDataLoadTask extends Task<List<Voucher>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPostingTxnDataLoadTask.class);

    private AccountPosting draftAccountPosting;

    public AccountPostingTxnDataLoadTask(AccountPosting draftAccountPosting) {
        this.draftAccountPosting = draftAccountPosting;
    }

    @Override
    protected List<Voucher> call() throws Exception {
        try {

            AccountPostingService accountPostingService = EmcsAppContext.getContext().getBean(AccountPostingService.class);
            return accountPostingService.loadAccountPostingData(draftAccountPosting);

        } catch (Exception e) {
            LOGGER.error("LedgerMappingEvent fetch", e);
            throw e;
        }
    }
}