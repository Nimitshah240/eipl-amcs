package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.StaffSalaryMapping;

import java.util.List;
import java.util.Optional;

public interface StaffSalaryMappingService {
    List<StaffSalaryMapping> findAll();

    String save(List<StaffSalaryMapping> staffSalaryMapping, String identityInfo);

    List<StaffSalaryMapping> update(List<StaffSalaryMapping> staffSalaryMapping, String identityInfo);

    Optional<StaffSalaryMapping> findById(String staffMemberName);

    void delete(String code, String identityInfo);

    void delete(StaffSalaryMapping staffSalaryMapping, String identityInfo);


}
