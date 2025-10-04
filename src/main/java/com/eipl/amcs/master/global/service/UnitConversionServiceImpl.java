package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.UnitConversion;
import com.eipl.amcs.master.global.repository.UnitConversionRepository;
import com.eipl.amcs.master.global.repository.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.unitConversionRepository;

@Service
public class UnitConversionServiceImpl implements UnitConversionService {

//	private UnitConversionRepository unitConversionRepository;

	private static final Logger log = LoggerFactory.getLogger(UnitConversionServiceImpl.class);


	@Override
	public List<UnitConversion> findAll() {
		List<UnitConversion> list = unitConversionRepository.findAll(Sort.by("code"));
		log.info("UnitConversion findAll {} items fetched", list.size());
		return list;
	}

}
