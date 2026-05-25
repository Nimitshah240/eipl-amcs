package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Message;
import com.eipl.amcs.master.operation.service.MessageService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class MessagesForDateAndShiftLoadTask extends Task<List<Message>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesForDateAndShiftLoadTask.class);
    private final LocalDate date;
    private final int shiftCode;

    public MessagesForDateAndShiftLoadTask(LocalDate date, int shiftCode) {
        this.date = date;
        this.shiftCode = shiftCode;
    }

    @Override
    protected List<Message> call() throws Exception {
        try {
            MessageService service = EmcsAppContext.getContext().getBean(MessageService.class);

            List<Message> list = service.findMessagesByDateAndShift(date, shiftCode);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Failed to fetch messages for date and shift", e);
        }
        return null;
    }
}
