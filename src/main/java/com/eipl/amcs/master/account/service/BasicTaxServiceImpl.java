package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.BasicTax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.basicTaxRepository;

@Service
public class BasicTaxServiceImpl implements BasicTaxService {
	
//	private BasicTaxRepository basicTaxRepository;

	private static final Logger log = LoggerFactory.getLogger(BasicTaxServiceImpl.class);

	@Override
	public List<BasicTax> findAll() {
		List<BasicTax> list = basicTaxRepository.findAll(Sort.by("name"));
		log.info("BasicTaxes findAll {} items fetched", list.size());
		return list;
	}

}