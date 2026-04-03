package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.dto.AccountPostingDto;
import com.eipl.amcs.setting.service.AccountPostingService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

public class AccountPostingLedgerMappingEventLoadTask extends Task<List<AccountPostingDto>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPostingLedgerMappingEventLoadTask.class);

    private int type;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private int eventType;

    public AccountPostingLedgerMappingEventLoadTask(int type, LocalDateTime fromDateTime, LocalDateTime toDateTime, int eventType) {
        this.type = type;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.eventType = eventType;
    }

    @Override
    protected List<AccountPostingDto> call() throws Exception {
        try {

            AccountPostingService accountPostingService = EmcsAppContext.getContext().getBean(AccountPostingService.class);
            if (eventType == 0)
                return accountPostingService.loadMilkCollectionAccountPosting(type, fromDateTime, toDateTime);
            else if (eventType == 1)
                return accountPostingService.loadLocalMilkSaleAccountPosting(type, fromDateTime, toDateTime);

        } catch (Exception e) {
            LOGGER.error("LedgerMappingEvent fetch", e);
            throw e;
        }
        return null;
    }
}