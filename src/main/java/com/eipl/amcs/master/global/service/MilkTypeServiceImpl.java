package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilkTypeServiceImpl implements MilkTypeService {

	@Autowired
	private MilkTypeRepository milkTypeRepository;

    private static final Logger log = LoggerFactory.getLogger(MilkTypeServiceImpl.class);

    @Override
    public List<MilkType> findAll() {
        List<MilkType> list = milkTypeRepository.findAll();
        log.info("MilkType findAll {} items fetched", list.size());
        return list;
    }

}
