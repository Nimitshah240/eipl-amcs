package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.repository.BmcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BmcServiceImpl implements BmcService {

    private static final Logger log = LoggerFactory.getLogger(BmcServiceImpl.class);
    @Autowired
    private BmcRepository bmcRepository;

    @Override
    public List<Bmc> findAll() {
        List<Bmc> list = bmcRepository.findAll(Sort.by("name"));
        log.info("Bmcs findAll {} items fetched", list.size());
        return list;
    }
}
