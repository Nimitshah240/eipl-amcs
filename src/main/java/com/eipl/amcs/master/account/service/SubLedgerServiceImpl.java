package com.eipl.amcs.master.account.service;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.master.account.repository.LedgerSubLedgerMappingRepository;
import com.eipl.amcs.master.account.repository.SubLedgerRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SubLedgerServiceImpl implements SubLedgerService {

    private static final Logger log = LoggerFactory.getLogger(SubLedgerServiceImpl.class);
    @Autowired
    private LedgerRepository ledgerRepository;
    @Autowired
    private SubLedgerRepository subLedgerRepository;
    @Autowired
    private SocietyRepository societyRepository;
    @Autowired
    private LedgerSubLedgerMappingRepository ledgerSubLedgerMappingRepository;

    @Override
    public List<SubLedger> findAll() {
        List<SubLedger> list = subLedgerRepository.findAll();
        log.info("SubLedgers findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public List<LedgerSubLedgerMapping> fetchMapping(String societyCode, String ledgerCode, String subLedgerCode) {
        Society society = societyRepository.findById(societyCode).orElseThrow(() -> new EntityNotFoundException(Society.class, "societyCode", societyCode));

        Ledger ledger = null;
        if (ledgerCode != null && !ledgerCode.isEmpty()) {
            ledger = ledgerRepository.findById(ledgerCode).orElseThrow(() -> new EntityNotFoundException(Ledger.class, "ledgerCode", ledgerCode));
            return ledgerSubLedgerMappingRepository.findBySocietyAndLedger(society, ledger);
        }

        SubLedger subLedger = null;
        if (subLedgerCode != null && !subLedgerCode.isEmpty()) {
            subLedger = subLedgerRepository.findById(subLedgerCode).orElseThrow(() -> new EntityNotFoundException(SubLedger.class, "subLedgerCode", subLedgerCode));
            return ledgerSubLedgerMappingRepository.findBySocietyAndSubLedger(society, subLedger);
        }
        return null;
    }


    @Override
    public SubLedger save(SubLedger subLedger, String identityInfo) {
        subLedger.setInitData();
        subLedger.setSociety(Hibernate.unproxy(subLedger.getSociety(), Society.class));
        SubLedger subLedger1 = subLedgerRepository.customSave(subLedger, identityInfo);
        subLedger1.setSociety(subLedger.getSociety());
        return subLedger1;
    }

    @Override
    public SubLedger update(SubLedger subLedger, String identityInfo) {
        subLedger.setupdateData();
        subLedger.setSociety(Hibernate.unproxy(subLedger.getSociety(), Society.class));
        SubLedger subLedger1 = subLedgerRepository.customUpdate(subLedger, identityInfo);
        subLedger1.setSociety(subLedger.getSociety());
        return subLedger1;
    }

    @Override
    public Optional<SubLedger> findById(String subLedgerTypeNo) {
        return subLedgerRepository.findById(subLedgerTypeNo);
    }

    @Override
    public void delete(String subLedgerTypeNo, String identityInfo) {
        SubLedger subLedger = subLedgerRepository.findById(subLedgerTypeNo).orElseThrow();
        subLedgerRepository.customDelete(subLedger, identityInfo);
    }

    @Override
    public void delete(SubLedger subLedger, String identityInfo) {
        subLedger.setSociety(Hibernate.unproxy(subLedger.getSociety(), Society.class));
        subLedgerRepository.customDelete(subLedger, identityInfo);
    }

}

