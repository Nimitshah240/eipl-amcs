package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.service.AccountPostingService;
import javafx.concurrent.Task;

import java.util.List;

public class AccountPostingSaveTask extends Task<AccountPosting> {

    private final AccountPosting accountPosting;
    private final List<Voucher> voucherList;

    public AccountPostingSaveTask(AccountPosting accountPosting, List<Voucher> voucherList) {
        this.accountPosting = accountPosting;
        this.voucherList = voucherList;
    }

    @Override
    protected AccountPosting call() throws Exception {
        try {
            AccountPostingService accountPostingService = EmcsAppContext.getContext().getBean(AccountPostingService.class);
            return accountPostingService.saveAccountPosting(accountPosting, voucherList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}