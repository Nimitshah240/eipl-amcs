package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Message;
import com.eipl.amcs.master.operation.repository.MessageRepository;
import javafx.concurrent.Task;

public class MessageSaveTask extends Task<Object> {

    private final Message dto;
    private final short update;

    public MessageSaveTask(Message dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MessageRepository messageRepository = EmcsAppContext.getContext().getBean(MessageRepository.class);
            NextCodeRepository nextCodeRepository = EmcsAppContext.getContext().getBean(NextCodeRepository.class);

            if (dto.getCode() == null || dto.getCode().isBlank()) {
                dto.setCode(nextCodeRepository.getNextCode("Message", "code", MainApp.identityDto.getSociety().getCode(), 0));
            }

            messageRepository.save(dto);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
