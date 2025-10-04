package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.master.geo.model.SubDistrict;

import java.util.List;

public interface SubDistrictService {

	List<SubDistrict> findAll();

	List<SubDistrict> findAll(String districtCode);

}
