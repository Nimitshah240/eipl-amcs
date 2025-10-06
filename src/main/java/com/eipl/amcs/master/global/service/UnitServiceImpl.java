package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.repository.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitRepository unitRepository;

    private static final Logger log = LoggerFactory.getLogger(UnitServiceImpl.class);

    @Override
    public List<Unit> findAll() {
        List<Unit> list = unitRepository.findAll(Sort.by("name"));
        log.info("Unit findAll {} items fetched", list.size());
        return list;
    }

}
