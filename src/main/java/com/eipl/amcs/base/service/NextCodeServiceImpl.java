package com.eipl.amcs.base.service;

import com.eipl.amcs.base.repository.IdentityRepository;
import com.eipl.amcs.base.repository.NextCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.nextCodeRepository;

@Service
public class NextCodeServiceImpl implements NextCodeService {

//	private NextCodeRepository nextCodeRepository;

	@Override
	public String getNextCode(String className, String pkColumnName, String prefix, int numberOfDigit) {
		return nextCodeRepository.getNextCode(className, pkColumnName, prefix, numberOfDigit);
	}

}
