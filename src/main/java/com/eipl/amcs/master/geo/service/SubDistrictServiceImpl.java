package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.repository.DistrictRepository;
import com.eipl.amcs.master.geo.repository.StateRepository;
import com.eipl.amcs.master.geo.repository.SubDistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.districtRepository;
import static com.eipl.amcs.config.BeanConfig.subDistRepository;

@Service
public class SubDistrictServiceImpl implements SubDistrictService {

//	private SubDistrictRepository subDistrictRepository;
//	private DistrictRepository districtRepository;



	@Override
	public List<SubDistrict> findAll() {
		return subDistRepository.findAll(Sort.by("name"));
	}
	
	@Override
	public List<SubDistrict> findAll(String districtCode) {
		District district= districtRepository.findById(districtCode)
				.orElseThrow(() -> new EntityNotFoundException(District.class, "invalid.districtcode"));
		return subDistRepository.findByDistrict(district, Sort.by("name"));
	}

}
