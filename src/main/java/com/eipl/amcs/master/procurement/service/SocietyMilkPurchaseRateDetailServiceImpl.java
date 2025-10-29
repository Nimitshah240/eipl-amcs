package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateDetail;
import com.eipl.amcs.master.procurement.repository.SocietyMilkPurchaseRateDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocietyMilkPurchaseRateDetailServiceImpl implements SocietyMilkPurchaseRateDetailService {

    private static final Logger log = LoggerFactory.getLogger(SocietyMilkPurchaseRateDetailServiceImpl.class);
    @Autowired
    private SocietyMilkPurchaseRateDetailRepository societyMilkPurchaseRateDetailRepository;

    @Override
    public List<SocietyMilkPurchaseRateDetail> findAll() {
        List<SocietyMilkPurchaseRateDetail> list = societyMilkPurchaseRateDetailRepository.findAll(Sort.by("code"));
        log.info("SocietyMilkPurchaseRateDetail findAll {} items fetched", list.size());
        return list;
    }
}
