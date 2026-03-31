package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.dto.AccountPostingDto;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.service.AccountPostingService;
import javafx.concurrent.Task;

import java.util.List;

public class AccountPostingSaveTask extends Task<AccountPosting> {

    private final AccountPosting accountPosting;
    private final List<AccountPostingDto> accountPostingDtoList;

    public AccountPostingSaveTask(AccountPosting accountPosting, List<AccountPostingDto> accountPostingDtoList) {
        this.accountPosting = accountPosting;
        this.accountPostingDtoList = accountPostingDtoList;
    }

    @Override
    protected AccountPosting call() throws Exception {
        try {

            AccountPostingService accountPostingService = EmcsAppContext.getContext().getBean(AccountPostingService.class);
            return accountPostingService.save(accountPosting, accountPostingDtoList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}