package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Events;
import com.eipl.amcs.master.account.repository.EventRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EventsLoadTask extends Task<List<Events>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsLoadTask.class);

    @Override
    protected List<Events> call() throws Exception {
        try {
            EventRepository eventRepository = EmcsAppContext.getContext().getBean(EventRepository.class);
            return eventRepository.findAll();
        } catch (Exception e) {
            LOGGER.error("FinancialYear fetch", e);
        }
        return null;
    }
}
