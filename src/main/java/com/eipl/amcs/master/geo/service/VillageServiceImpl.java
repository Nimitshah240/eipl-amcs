package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.model.Village;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.subDistRepository;
import static com.eipl.amcs.config.BeanConfig.villageRepository;

@Service
public class VillageServiceImpl implements VillageService {

//	private VillageRepository villageRepository;
//	private SubDistrictRepository subDistRepository;


	@Override
	public List<Village> findAll() {
		return villageRepository.findAll(Sort.by("name"));
	}

	@Override
	public List<Village> findAll(String subDistrictCode) {
		SubDistrict subDistrict = subDistRepository.findById(subDistrictCode)
				.orElseThrow(() -> new EntityNotFoundException(SubDistrict.class, "invalid.subdistrictcode"));
		return villageRepository.findBySubDistrict(subDistrict, Sort.by("name"));
	}
}
