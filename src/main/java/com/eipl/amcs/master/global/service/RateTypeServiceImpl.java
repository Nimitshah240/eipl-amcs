package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.repository.RateTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateTypeServiceImpl implements RateTypeService {

    @Autowired
    private RateTypeRepository rateTypeRepository;

    @Override
    public List<RateType> findAll() {
        return rateTypeRepository.findAll();
    }

}
