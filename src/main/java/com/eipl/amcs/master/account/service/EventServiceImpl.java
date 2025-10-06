package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.Events;
import com.eipl.amcs.master.account.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    @Override
    public List<Events> findAll() {
        List<Events> list = eventRepository.findAll(Sort.by("code"));
        log.info("Events findAll {} items fetched", list.size());
        return list;
    }

}