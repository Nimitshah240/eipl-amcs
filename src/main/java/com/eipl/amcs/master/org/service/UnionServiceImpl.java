package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.repository.UnionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UnionServiceImpl implements UnionService {

    private static final Logger log = LoggerFactory.getLogger(UnionServiceImpl.class);
    @Autowired
    private UnionRepository unionRepository;

    @Override
    public List<Union> findAll() {
        List<Union> list = unionRepository.findAll(Sort.by("name"));
        log.info("Unions findAll {} items fetched", list.size());
        return list;
    }
}
