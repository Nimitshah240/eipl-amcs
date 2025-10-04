package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.StaffSalary;
import com.eipl.amcs.master.account.repository.StaffSalaryProcessRepository;
import com.eipl.amcs.master.account.repository.StaffSalaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.staffSalaryRepository;

@Service
public class StaffSalaryServiceImpl implements StaffSalaryService {

//    private StaffSalaryRepository staffSalaryRepository;

    private static final Logger log = LoggerFactory.getLogger(StaffSalaryServiceImpl.class);

    @Override
    public List<StaffSalary> findAll() {
        List<StaffSalary> list = staffSalaryRepository.findAll(Sort.by("name"));
        log.info("LedgerTypes findAll {} items fetched", list.size());
        return list;
    }
}
