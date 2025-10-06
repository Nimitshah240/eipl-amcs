package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.repository.GenderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenderServiceImpl implements GenderService {

	@Autowired
	private GenderRepository genderRepository;

	private static final Logger log = LoggerFactory.getLogger(GenderServiceImpl.class);

	@Override
	public List<Gender> findAll() {
		List<Gender> list = genderRepository.findAll();
		log.info("Gender findAll {} items fetched", list.size());
		return list;
	}

}
