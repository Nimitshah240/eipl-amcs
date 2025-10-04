package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.repository.DistrictRepository;
import com.eipl.amcs.master.geo.repository.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private StateRepository stateRepository;

    private static final Logger log = LoggerFactory.getLogger(DistrictServiceImpl.class);

    @Override
    public List<District> findAll() {
        List<District> list = districtRepository.findAll(Sort.by("name"));
        log.info("Districts findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public List<District> findAll(String stateCode) {
        State state = stateRepository.findById(stateCode)
                .orElseThrow(() -> new EntityNotFoundException(State.class, "invalid.statecode"));
        return districtRepository.findByState(state, Sort.by("name"));
    }

}
