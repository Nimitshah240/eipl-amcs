package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Plant;
import com.eipl.amcs.master.org.repository.PlantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PlantServiceImpl implements PlantService {

	@Autowired
	private PlantRepository plantRepository;

	private static final Logger log = LoggerFactory.getLogger(PlantServiceImpl.class);

	@Override
	public List<Plant> findAll() {
		List<Plant> list = plantRepository.findAll(Sort.by("name"));
		log.info("Plants findAll {} items fetched", list.size());
		return list;
	}
}
