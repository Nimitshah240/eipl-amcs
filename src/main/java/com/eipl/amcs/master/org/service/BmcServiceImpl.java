package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.repository.BmcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.bmcRepository;

@Service
public class BmcServiceImpl implements BmcService {
//	@Autowired
//	private BmcRepository bmcRepository;

	private static final Logger log = LoggerFactory.getLogger(BmcServiceImpl.class);

	@Override
	public List<Bmc> findAll() {
		List<Bmc> list = bmcRepository.findAll(Sort.by("name"));
		log.info("Bmcs findAll {} items fetched", list.size());
		return list;
	}
}
