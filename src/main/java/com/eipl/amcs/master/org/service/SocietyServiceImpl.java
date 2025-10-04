package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.societyRepository;

@Service
public class SocietyServiceImpl implements SocietyService {
//	@Autowired
//	private SocietyRepository societyRepository;

	private static final Logger log = LoggerFactory.getLogger(SocietyServiceImpl.class);

	@Override
	public List<Society> findAll() {
		List<Society> list = societyRepository.findAll(Sort.by("name"));
		log.info("Societys findAll {} items fetched", list.size());
		return list;
	}

	@Override
	public Society save(Society obj) {
		societyRepository.save(obj);
		return null;
	}
}
