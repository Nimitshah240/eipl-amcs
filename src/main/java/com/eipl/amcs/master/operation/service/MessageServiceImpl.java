package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.master.operation.model.Message;
import com.eipl.amcs.master.operation.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void deleteById(String code) {
        messageRepository.deleteById(code);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> findAllWithShifts() {
        return messageRepository.findAllWithShifts();
    }

    @Override
    public List<Message> findMessagesByDateAndShift(LocalDate date, int shiftCode) {
        return messageRepository.findMessagesByDateAndShift(date, shiftCode);
    }
}
