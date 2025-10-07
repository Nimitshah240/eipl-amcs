package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.master.operation.model.MemberCreditLimit;
import com.eipl.amcs.master.operation.repository.MemberCreditLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberCreditLimitServiceImpl implements MemberCreditLimitService {
	@Autowired
	private MemberCreditLimitRepository repository;

	@Override
	public Optional<MemberCreditLimit> findByConsumerCodeAndConsumerType(String consumerCode, Short consumerType) {
		return repository.findByConsumerCodeAndConsumerType(consumerCode, consumerType);
	}
}