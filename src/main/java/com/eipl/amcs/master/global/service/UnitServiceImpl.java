package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.geo.repository.SubDistrictRepository;
import com.eipl.amcs.master.geo.repository.VillageRepository;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.repository.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.unitRepository;

@Service
public class UnitServiceImpl implements UnitService {

//	private UnitRepository unitRepository;

	private static final Logger log = LoggerFactory.getLogger(UnitServiceImpl.class);

	@Override
	public List<Unit> findAll() {
		List<Unit> list = unitRepository.findAll(Sort.by("name"));
		log.info("Unit findAll {} items fetched", list.size());
		return list;
	}

}
