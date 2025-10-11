package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.repository.MemberTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberTypeServiceImpl implements MemberTypeService {

    @Autowired
    private MemberTypeRepository memberTypeRepository;

    private static final Logger log = LoggerFactory.getLogger(MemberTypeServiceImpl.class);

    @Override
    public List<MemberType> findAll() {
        List<MemberType> list = memberTypeRepository.findAll();
        log.info("MemberTypes findAll {} items fetched", list.size());
        return list;
    }

}
