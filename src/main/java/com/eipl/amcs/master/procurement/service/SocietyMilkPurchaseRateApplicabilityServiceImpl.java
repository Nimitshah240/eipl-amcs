package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateApplicability;
import com.eipl.amcs.master.procurement.repository.SocietyMilkPurchaseRateApplicabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocietyMilkPurchaseRateApplicabilityServiceImpl implements SocietyMilkPurchaseRateApplicabilityService {

    private static final Logger log = LoggerFactory.getLogger(SocietyMilkPurchaseRateApplicabilityServiceImpl.class);
    @Autowired
    private SocietyMilkPurchaseRateApplicabilityRepository societyMilkPurchaseRateApplicabilityRepository;

    @Override
    public List<SocietyMilkPurchaseRateApplicability> findAll() {
        List<SocietyMilkPurchaseRateApplicability> list = societyMilkPurchaseRateApplicabilityRepository.findAll(Sort.by("code"));
        log.info("SocietyMilkPurchaseRateApplicability findAll {} items fetched", list.size());
        return list;
    }
}
