package com.eipl.amcs.base.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.sync.repository.BroadcastedRepository;
import javafx.concurrent.Task;

public class PendingSyncTask extends Task<Long> {

    @Override
    protected Long call() throws Exception {
        try {
            BroadcastedRepository repository = EmcsAppContext.getContext().getBean(BroadcastedRepository.class);
            return repository.count();
        } catch (Exception e) {
            return 0L;
        }
    }
}