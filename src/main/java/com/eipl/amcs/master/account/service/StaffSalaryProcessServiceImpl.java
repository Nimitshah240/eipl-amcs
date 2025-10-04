package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.StaffSalaryProcess;
import com.eipl.amcs.master.account.repository.StaffSalaryMappingRepository;
import com.eipl.amcs.master.account.repository.StaffSalaryProcessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.staffSalaryProcessRepository;

@Service
public class StaffSalaryProcessServiceImpl implements StaffSalaryProcessService {

//    private StaffSalaryProcessRepository staffSalaryProcessRepository;

    private static final Logger log = LoggerFactory.getLogger(StaffSalaryProcessServiceImpl.class);


    @Override
    public List<StaffSalaryProcess> findAll() {
        List<StaffSalaryProcess> list = staffSalaryProcessRepository.findAll(Sort.by("name"));
        log.info("StaffSalaryProcess findAll {} items fetched", list.size());
        return list;
    }
}
