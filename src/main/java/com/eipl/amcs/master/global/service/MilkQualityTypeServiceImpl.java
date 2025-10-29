package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.repository.MilkQualityTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilkQualityTypeServiceImpl implements MilkQualityTypeService {

    private static final Logger log = LoggerFactory.getLogger(MilkQualityTypeServiceImpl.class);
    @Autowired
    private MilkQualityTypeRepository milkQualityRepository;

    @Override
    public List<MilkQualityType> findAll() {
        List<MilkQualityType> list = milkQualityRepository.findAll();
        log.info("MilkQualityType findAll {} items fetched", list.size());
        return list;
    }

}
