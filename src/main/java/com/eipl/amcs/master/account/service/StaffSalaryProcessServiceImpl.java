package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.StaffSalaryProcess;
import com.eipl.amcs.master.account.repository.StaffSalaryProcessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffSalaryProcessServiceImpl implements StaffSalaryProcessService {

    private static final Logger log = LoggerFactory.getLogger(StaffSalaryProcessServiceImpl.class);
    @Autowired
    private StaffSalaryProcessRepository staffSalaryProcessRepository;

    @Override
    public List<StaffSalaryProcess> findAll() {
        List<StaffSalaryProcess> list = staffSalaryProcessRepository.findAll(Sort.by("name"));
        log.info("StaffSalaryProcess findAll {} items fetched", list.size());
        return list;
    }
}
