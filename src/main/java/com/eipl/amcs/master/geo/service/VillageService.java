package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.master.geo.model.Village;

import java.util.List;

public interface VillageService {

	List<Village> findAll();

	List<Village> findAll(String subDistrictCode);

}
