package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Society;

import java.util.List;

public interface SocietyService {
    List<Society> findAll();

    Society save(Society obj);

}
