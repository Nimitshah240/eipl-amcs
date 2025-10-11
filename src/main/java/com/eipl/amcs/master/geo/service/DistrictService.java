package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.master.geo.model.District;

import java.util.List;

public interface DistrictService {

    List<District> findAll();

    List<District> findAll(String stateCode);

}
