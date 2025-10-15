package com.eipl.amcs.utils.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.sync.producer.BroadcastedService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BroadcastedTask extends Task<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastedTask.class);

    @Override
    protected Void call() throws Exception {
        try {
            BroadcastedService broadcastedService = EmcsAppContext.getContext().getBean(BroadcastedService.class);
            broadcastedService.sendBroadcastedAll();
            return null;
        } catch (Exception e) {
            LOGGER.error("Broadcast Data All In One", e);
        }
        return null;
    }
}

