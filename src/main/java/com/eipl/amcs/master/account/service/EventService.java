package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.Events;

import java.util.List;

public interface EventService {
    List<Events> findAll();

}
