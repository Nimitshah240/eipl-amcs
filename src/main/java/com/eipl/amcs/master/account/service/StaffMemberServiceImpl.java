package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.master.account.repository.StaffMemberRepository;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StaffMemberServiceImpl implements StaffMemberService {

    private static final Logger log = LoggerFactory.getLogger(StaffMemberServiceImpl.class);
    @Autowired
    private StaffMemberRepository staffMemberRepository;

    @Override
    public List<StaffMember> findAll() {
        List<StaffMember> list = staffMemberRepository.findAll(Sort.by("name"));
        log.info("StaffMember findAll {} items fetched", list.size());
        for (StaffMember staffMember : list) {
            staffMember.setBank(Hibernate.unproxy(staffMember.getBank(), Bank.class));
            staffMember.setSociety(Hibernate.unproxy(staffMember.getSociety(), Society.class));
            staffMember.setGender(Hibernate.unproxy(staffMember.getGender(), Gender.class));
            staffMember.setDesignation(Hibernate.unproxy(staffMember.getDesignation(), Designation.class));
            staffMember.setBranch(Hibernate.unproxy(staffMember.getBranch(), Branch.class));
        }
        return list;
    }

    @Override
    public StaffMember save(StaffMember staffMember, String identityInfo) {
        staffMember.setSociety(Hibernate.unproxy(staffMember.getSociety(), Society.class));
        staffMember.setBank(Hibernate.unproxy(staffMember.getBank(), Bank.class));


        staffMember.setInitData();
        StaffMember staffMember1 = staffMemberRepository.save(staffMember);
        staffMember1.setSociety(Hibernate.unproxy(staffMember1.getSociety(), Society.class));
        staffMember1.setDesignation(Hibernate.unproxy(staffMember1.getDesignation(), Designation.class));
        staffMember1.setGender(Hibernate.unproxy(staffMember1.getGender(), Gender.class));
        staffMember1.setBank(Hibernate.unproxy(staffMember1.getBank(), Bank.class));
        staffMember1.setBranch(Hibernate.unproxy(staffMember1.getBranch(), Branch.class));
        return staffMember1;
    }

    @Override
    public StaffMember update(StaffMember staffMember, String identityInfo) {
        staffMember.setSociety(Hibernate.unproxy(staffMember.getSociety(), Society.class));
        staffMember.setBank(Hibernate.unproxy(staffMember.getBank(), Bank.class));


        staffMember.setupdateData();
        StaffMember staffMember1 = staffMemberRepository.save(staffMember);
        staffMember1.setSociety(Hibernate.unproxy(staffMember1.getSociety(), Society.class));
        staffMember1.setDesignation(Hibernate.unproxy(staffMember1.getDesignation(), Designation.class));
        staffMember1.setGender(Hibernate.unproxy(staffMember1.getGender(), Gender.class));
        staffMember1.setBank(Hibernate.unproxy(staffMember1.getBank(), Bank.class));
        staffMember1.setBranch(Hibernate.unproxy(staffMember1.getBranch(), Branch.class));
        return staffMember1;
    }

    @Override
    public Optional<StaffMember> findById(String staffMemberNo) {
        return Optional.empty();
    }

    @Override
    public void delete(String staffMemberNo, String identityInfo) {
        staffMemberRepository.deleteById(staffMemberNo);
    }

    @Override
    public void delete(StaffMember staffMember, String identityInfo) {

        staffMemberRepository.delete(staffMember);
    }

}