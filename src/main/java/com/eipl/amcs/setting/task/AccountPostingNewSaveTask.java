package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.service.AccountPostingServiceNew;
import javafx.concurrent.Task;

import java.util.List;

public class AccountPostingNewSaveTask extends Task<AccountPosting> {

    private final AccountPosting accountPosting;
    private final List<Voucher> voucherList;

    public AccountPostingNewSaveTask(AccountPosting accountPosting, List<Voucher> voucherList) {
        this.accountPosting = accountPosting;
        this.voucherList = voucherList;
    }

    @Override
    protected AccountPosting call() throws Exception {
        try {
            AccountPostingServiceNew accountPostingService = EmcsAppContext.getContext().getBean(AccountPostingServiceNew.class);
            return accountPostingService.saveAccountPosting(accountPosting, voucherList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}