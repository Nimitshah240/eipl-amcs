package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.master.operation.model.Message;

import java.time.LocalDate;
import java.util.List;

public interface MessageService {
    void deleteById(String code);

    List<Message> findAll();

    List<Message> findAllWithShifts();

    List<Message> findMessagesByDateAndShift(LocalDate date, int shiftCode);
}