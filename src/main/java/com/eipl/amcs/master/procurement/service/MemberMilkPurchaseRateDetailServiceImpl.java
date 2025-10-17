package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateDetail;
import com.eipl.amcs.master.procurement.repository.MemberMilkPurchaseRateDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberMilkPurchaseRateDetailServiceImpl implements MemberMilkPurchaseRateDetailService {

    private static final Logger log = LoggerFactory.getLogger(MemberMilkPurchaseRateDetailServiceImpl.class);
    @Autowired
    private MemberMilkPurchaseRateDetailRepository dtlRepository;

    @Override
    public List<MemberMilkPurchaseRateDetail> findAll() {
        List<MemberMilkPurchaseRateDetail> list = dtlRepository.findAll(Sort.by("code"));
        log.info("MemberMilkPurchaseRateDetails findAll {} items fetched", list.size());
        return list;
    }
}
