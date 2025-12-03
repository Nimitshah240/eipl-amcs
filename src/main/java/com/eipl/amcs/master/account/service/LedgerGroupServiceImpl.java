package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.repository.LedgerGroupRepository;
import com.eipl.amcs.master.account.repository.LedgerTypeRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LedgerGroupServiceImpl implements LedgerGroupService {

    private static final Logger log = LoggerFactory.getLogger(LedgerGroupServiceImpl.class);
    @Autowired
    private LedgerGroupRepository ledgerGroupRepository;
    @Autowired
    private LedgerTypeRepository typeRepository;

    @Override
    public List<LedgerGroup> findAll() {
        List<LedgerGroup> list = ledgerGroupRepository.findAllByActive(true, Sort.by("code"));
        for (LedgerGroup ledgerGroup : list) {
            ledgerGroup.setLedgerType(Hibernate.unproxy(ledgerGroup.getLedgerType(), LedgerType.class));
        }
        log.info("LedgerGroups findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public LedgerGroup save(LedgerGroup ledgerGroup, String identityInfo) {
        ledgerGroup.setInitData();
        LedgerGroup ledgerGroup1 = ledgerGroupRepository.customSave(ledgerGroup, identityInfo);
        ledgerGroup1.setLedgerType(ledgerGroup.getLedgerType());
        return ledgerGroup1;
    }

    @Override
    public LedgerGroup update(LedgerGroup ledgerGroup, String identityInfo) {
        ledgerGroup.setupdateData();
        return ledgerGroupRepository.customUpdate(ledgerGroup, identityInfo);
    }

    @Override
    public Optional<LedgerGroup> findById(String ledgerTypeNo) {
        return Optional.empty();
    }

    @Override
    public void delete(String ledgerTypeNo, String identityInfo) {
        LedgerGroup ledgerGroup = ledgerGroupRepository.findById(ledgerTypeNo).get();
        ledgerGroup.setLedgerType(Hibernate.unproxy(ledgerGroup.getLedgerType(), LedgerType.class));
        ledgerGroupRepository.customDelete(ledgerGroup, identityInfo);
    }

    @Override
    public void delete(LedgerGroup ledgerGroup, String identityInfo) {
        ledgerGroup.setLedgerType(Hibernate.unproxy(ledgerGroup.getLedgerType(), LedgerType.class));
        ledgerGroupRepository.customDelete(ledgerGroup, identityInfo);
    }

    @Override
    public List<LedgerGroup> findByLedgerType(String code) {
        LedgerType ledgerType = typeRepository.findById(code).get();
        if (ledgerType != null)
            return ledgerGroupRepository.findByLedgerTypeAndActive(ledgerType, true);
        else
            return null;
    }

}