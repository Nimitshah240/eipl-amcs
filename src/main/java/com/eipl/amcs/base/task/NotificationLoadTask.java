package com.eipl.amcs.base.task;

import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.base.service.NotificationService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NotificationLoadTask extends Task<List<Notification>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationLoadTask.class);

    @Override
    protected List<Notification> call() throws Exception {
        try {
            NotificationService service = EmcsAppContext.getContext().getBean(NotificationService.class);
            List<Notification> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Notification fetch", e);
        }
        return null;
    }
}
