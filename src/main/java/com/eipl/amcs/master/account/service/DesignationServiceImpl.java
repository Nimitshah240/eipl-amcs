package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.Designation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.designationRepository;

@Service
public class DesignationServiceImpl implements DesignationService {

//	private DesignationRepository repositoryrepository;

	private static final Logger log = LoggerFactory.getLogger(DesignationServiceImpl.class);

	@Override
	public List<Designation> findAll() {
		List<Designation> list = designationRepository.findAll(Sort.by("name"));
		log.info("Designation findAll {} items fetched", list.size());
		return list;
	}

}