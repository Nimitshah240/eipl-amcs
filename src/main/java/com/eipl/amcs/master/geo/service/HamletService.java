package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.master.geo.model.Hamlet;

import java.util.List;

public interface HamletService {

    List<Hamlet> findAll();

    List<Hamlet> findAll(String villageCode);

}
