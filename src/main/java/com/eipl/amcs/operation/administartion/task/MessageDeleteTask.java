package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.service.MessageService;
import javafx.concurrent.Task;

public class MessageDeleteTask extends Task<Boolean> {
    private final String code;

    public MessageDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MessageService service = EmcsAppContext.getContext().getBean(MessageService.class);
            service.deleteById(code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}