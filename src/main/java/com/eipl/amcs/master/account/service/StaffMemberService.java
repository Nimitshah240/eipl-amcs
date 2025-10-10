package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.StaffMember;

import java.util.List;
import java.util.Optional;

public interface StaffMemberService {
    List<StaffMember> findAll();


    StaffMember save(StaffMember staffMember, String identityInfo);

    StaffMember update(StaffMember staffMember, String identityInfo);


    Optional<StaffMember> findById(String staffMemberNo);

    void delete(String staffMemberNo, String identityInfo);

    void delete(StaffMember staffMember, String identityInfo);
}
