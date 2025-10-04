package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.CommitteeMembers;

import java.util.List;

public interface CommitteeMembersService {
    List<CommitteeMembers> findAll();

    CommitteeMembers findAllByCommitteeMembers(String memberCode);

    CommitteeMembers save(CommitteeMembers committeeMembers, String identityInfo);

    CommitteeMembers update(CommitteeMembers committeeMembers, String identityInfo);

    void  delete(String memberCode,String identityInfo);

}
