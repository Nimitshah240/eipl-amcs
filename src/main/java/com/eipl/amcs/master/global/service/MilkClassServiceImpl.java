package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.repository.GenderRepository;
import com.eipl.amcs.master.global.repository.MilkClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.milkClassRepository;

@Service
public class MilkClassServiceImpl implements MilkClassService {

//	@Autowired
//	private final MilkClassRepository milkClassRepository;

	private static final Logger log = LoggerFactory.getLogger(MilkClassServiceImpl.class);

	@Override
	public List<MilkClass> findAll() {
		List<MilkClass> list = milkClassRepository.findAll();
		log.info("MilkClass findAll {} items fetched", list.size());
		return list;
	}

}
