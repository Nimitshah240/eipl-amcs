package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.StaffSalaryHead;
import com.eipl.amcs.master.account.repository.StaffSalaryHeadRepository;
import com.eipl.amcs.master.org.model.Society;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class StaffSalaryHeadServiceImpl implements StaffSalaryHeadService {

    private static final Logger log = LoggerFactory.getLogger(StaffSalaryHeadServiceImpl.class);
    @Autowired
    private StaffSalaryHeadRepository staffSalaryHeadRepository;

    @Override
    public List<StaffSalaryHead> findAll() {
        List<StaffSalaryHead> list = staffSalaryHeadRepository.findAll(Sort.by("name"));
        log.info("StaffSalaryHead findAll {} items fetched", list.size());
        return list;
    }

    @Override
    @Transactional
    public StaffSalaryHead save(StaffSalaryHead staffSalaryHead, String identityInfo) {
        staffSalaryHead.setInitData();
        StaffSalaryHead staffSalaryHead1 = staffSalaryHeadRepository.save(staffSalaryHead);
        staffSalaryHead1.setSociety(Hibernate.unproxy(staffSalaryHead1.getSociety(), Society.class));
        return staffSalaryHead1;
    }

    @Override
    public StaffSalaryHead update(StaffSalaryHead staffSalaryHead, String identityInfo) {
        staffSalaryHead.setupdateData();
        return staffSalaryHeadRepository.save(staffSalaryHead);
    }

    @Override
    public Optional<StaffSalaryHead> findById(String staffMemberName) {
        return Optional.empty();
    }

    @Override
    public void delete(String staffMemberName, String identityInfo) {
        staffSalaryHeadRepository.deleteById(Integer.valueOf(staffMemberName));
    }

    @Override
    public void delete(StaffSalaryHead staffSalaryHead, String identityInfo) {
        staffSalaryHeadRepository.delete(staffSalaryHead);
    }

}