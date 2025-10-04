package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.MemberType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.memberTypeRepository;

@Service
public class MemberTypeServiceImpl implements MemberTypeService {

//	private MemberTypeRepository memberTypeRepository;

	private static final Logger log = LoggerFactory.getLogger(MemberTypeServiceImpl.class);

	@Override
	public List<MemberType> findAll() {
		List<MemberType> list = memberTypeRepository.findAll();
		log.info("MemberTypes findAll {} items fetched", list.size());
		return list;
	}

}
