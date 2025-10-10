package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.StaffSalaryHead;

import java.util.List;
import java.util.Optional;

public interface StaffSalaryHeadService {
    List<StaffSalaryHead> findAll();

    StaffSalaryHead save(StaffSalaryHead staffSalaryHead, String identityInfo);

    StaffSalaryHead update(StaffSalaryHead staffSalaryHead, String identityInfo);

    Optional<StaffSalaryHead> findById(String staffMemberName);

    void delete(String code, String identityInfo);

    void delete(StaffSalaryHead staffSalaryHead, String identityInfo);


}
