package com.eipl.amcs.master.global.service;

import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.repository.ShiftRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftServiceImpl implements ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    private static final Logger log = LoggerFactory.getLogger(ShiftServiceImpl.class);

    @Override
    public List<Shift> findAll() {
        List<Shift> list = shiftRepository.findAll();
        log.info("Shift findAll {} items fetched", list.size());
        return list;
    }

}
