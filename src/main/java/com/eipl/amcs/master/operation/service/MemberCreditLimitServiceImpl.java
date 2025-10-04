package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.master.operation.model.MemberCreditLimit;
import com.eipl.amcs.master.operation.repository.MemberCreditLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.eipl.amcs.config.BeanConfig.memberCreditLimitRepository;

@Service
public class MemberCreditLimitServiceImpl implements MemberCreditLimitService {
//	@Autowired
//	private MemberCreditLimitRepository memberCreditLimitRepository;

	@Override
	public Optional<MemberCreditLimit> findByConsumerCodeAndConsumerType(String consumerCode, Short consumerType) {
		return memberCreditLimitRepository.findByConsumerCodeAndConsumerType(consumerCode, consumerType);
	}
}