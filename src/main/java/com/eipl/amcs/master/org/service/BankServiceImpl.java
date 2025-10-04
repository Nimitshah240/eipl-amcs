package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.repository.BankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.bankRepository;

@Service
public class BankServiceImpl implements BankService {
//	@Autowired
//	private BankRepository bankRepository;

	private static final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

	@Override
	public List<Bank> findAll() {
		List<Bank> list = bankRepository.findAll(Sort.by("name"));
		log.info("Banks findAll {} items fetched", list.size());
		return list;
	}
}
