package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.repository.LedgerTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LedgerTypeServiceImpl implements LedgerTypeService {

    @Autowired
    private LedgerTypeRepository typeRepository;

    private static final Logger log = LoggerFactory.getLogger(LedgerTypeServiceImpl.class);


    @Override
    public List<LedgerType> findAll() {
        List<LedgerType> list = typeRepository.findAll(Sort.by("code"));
        log.info("LedgerTypes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public LedgerType save(LedgerType ledgerType, String identityInfo) {
        ledgerType.setInitData();
        return typeRepository.customSave(ledgerType, identityInfo);
    }

    @Override
    public LedgerType update(LedgerType ledgerType, String identityInfo) {
        ledgerType.setupdateData();
        return typeRepository.customUpdate(ledgerType, identityInfo);
    }

    @Override
    public Optional<LedgerType> findById(String ledgerTypeNo) {
        return Optional.empty();
    }

    @Override
    public void delete(Integer ledgerTypeNo, String identityInfo) {
        typeRepository.customDelete(ledgerTypeNo, identityInfo);
    }

    @Override
    public void delete(LedgerType ledgerType, String identityInfo) {
        typeRepository.customDelete(ledgerType, identityInfo);
    }

}