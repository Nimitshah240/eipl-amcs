package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.master.account.repository.CommitteeMembersRepository;
import com.eipl.amcs.master.account.repository.CommitteeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    private static final Logger log = LoggerFactory.getLogger(CommitteeServiceImpl.class);
    @Autowired
    private CommitteeMembersRepository committeeMembersRepository;
    @Autowired
    private CommitteeRepository committeeRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;


    public String nextCode(String societyCode) {
        return nextCodeRepository.getNextCode("Committee", "code", societyCode, 0);
    }

    @Transactional
    public Boolean deleteById(String code) {
        try {
            committeeMembersRepository.deleteByCommittee_Code(code);
            committeeRepository.deleteById(code);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

