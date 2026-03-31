package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.repository.AccountPostingRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class AccountPostingLoadTask extends Task<List<AccountPosting>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPostingLoadTask.class);

    private LocalDate fromDate;
    private LocalDate toDate;

    public AccountPostingLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<AccountPosting> call() throws Exception {
        try {
            AccountPostingRepository accountPostingRepository = EmcsAppContext.getContext().getBean(AccountPostingRepository.class);
            return accountPostingRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(fromDate, toDate);
        } catch (Exception e) {
            LOGGER.error("LedgerMappingEvent fetch", e);
        }
        return null;
    }
}