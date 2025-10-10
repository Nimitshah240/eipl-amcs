package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Mcc;
import com.eipl.amcs.master.org.repository.MccRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MccServiceImpl implements MccService {

    @Autowired
    private MccRepository mccRepository;

    private static final Logger log = LoggerFactory.getLogger(MccServiceImpl.class);

    @Override
    public List<Mcc> findAll() {
        List<Mcc> list = mccRepository.findAll(Sort.by("name"));
        log.info("Mccs findAll {} items fetched", list.size());
        return list;
    }
}
