package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateApplicability;
import com.eipl.amcs.master.procurement.repository.MemberMilkPurchaseRateApplicabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.memberMilkPurchaseRateApplicabilityRepository;

@Service
public class MemberMilkPurchaseRateApplicabilityServiceImpl implements MemberMilkPurchaseRateApplicabilityService {

//	@Autowired
//	private MemberMilkPurchaseRateApplicabilityRepository memberMilkPurchaseRateApplicabilityRepository;

	private static final Logger log = LoggerFactory.getLogger(MemberMilkPurchaseRateApplicabilityServiceImpl.class);

	@Override
	public List<MemberMilkPurchaseRateApplicability> findAll() {
		List<MemberMilkPurchaseRateApplicability> list = memberMilkPurchaseRateApplicabilityRepository.findAll(Sort.by("code"));
		log.info("MemberMilkPurchaseRateApplicability findAll {} items fetched", list.size());
		return list;
	}
}
