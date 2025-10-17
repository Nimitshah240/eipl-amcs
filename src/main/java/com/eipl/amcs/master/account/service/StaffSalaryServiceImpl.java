package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.StaffSalary;
import com.eipl.amcs.master.account.repository.StaffSalaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StaffSalaryServiceImpl implements StaffSalaryService {

    private static final Logger log = LoggerFactory.getLogger(StaffSalaryServiceImpl.class);
    @Autowired
    private StaffSalaryRepository staffSalaryRepository;

    @Override
    public List<StaffSalary> findAll() {
        List<StaffSalary> list = staffSalaryRepository.findAll(Sort.by("name"));
        log.info("LedgerTypes findAll {} items fetched", list.size());
        return list;
    }
}
