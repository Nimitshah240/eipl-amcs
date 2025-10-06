package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.master.account.repository.DesignationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DesignationServiceImpl implements DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    private static final Logger log = LoggerFactory.getLogger(DesignationServiceImpl.class);

    @Override
    public List<Designation> findAll() {
        List<Designation> list = designationRepository.findAll(Sort.by("name"));
        log.info("Designation findAll {} items fetched", list.size());
        return list;
    }

}