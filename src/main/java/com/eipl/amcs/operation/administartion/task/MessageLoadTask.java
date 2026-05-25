package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Message;
import com.eipl.amcs.master.operation.service.MessageService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageLoadTask extends Task<List<Message>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageLoadTask.class);

    @Override
    protected List<Message> call() throws Exception {
        try {
            MessageService service = EmcsAppContext.getContext().getBean(MessageService.class);
            List<Message> list = service.findAllWithShifts();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Messages fetch", e);
        }
        return null;
    }
}