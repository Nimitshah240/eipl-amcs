package com.eipl.amcs.master.account.service;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerGroup;
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
public class LedgerServiceImpl implements LedgerService {

    @Autowired
    private LedgerRepository ledgerRepository;
    @Autowired
    private SubLedgerRepository subLedgerRepository;
    @Autowired
    private LedgerSubLedgerMappingRepository ledgerSubLedgerMappingRepository;
    @Autowired
    private SocietyRepository societyRepository;

    private static final Logger log = LoggerFactory.getLogger(LedgerServiceImpl.class);


    @Override
    public List<Ledger> findAll() {
        List<Ledger> list = ledgerRepository.findAllByCode();
        log.info("Ledgers findAll {} items fetched", list.size());
        for (Ledger ledger : list) {
            ledger.setLedgerGroup(Hibernate.unproxy(ledger.getLedgerGroup(), LedgerGroup.class));
            ledger.setSociety(Hibernate.unproxy(ledger.getSociety(), Society.class));
        }
        return list;
    }

    @Override
    public List<LedgerSubLedgerMapping> fetchMapping(String societyCode, String ledgerCode, String subLedgerCode) {
        Society society = societyRepository.findById(societyCode)
                .orElseThrow(() -> new EntityNotFoundException(Society.class, "societyCode", societyCode));

        Ledger ledger = null;
        if (ledgerCode != null && !ledgerCode.isEmpty()) {
            ledger = ledgerRepository.findById(ledgerCode)
                    .orElseThrow(() -> new EntityNotFoundException(Ledger.class, "ledgerCode", ledgerCode));

            return ledgerSubLedgerMappingRepository.findBySocietyAndLedger(society, ledger);
        }

        SubLedger subLedger = null;
        if (subLedgerCode != null && !subLedgerCode.isEmpty()) {
            subLedger = subLedgerRepository.findById(subLedgerCode)
                    .orElseThrow(() -> new EntityNotFoundException(SubLedger.class, "subLedgerCode", subLedgerCode));

            return ledgerSubLedgerMappingRepository.findBySocietyAndSubLedger(society, subLedger);
        }

        return null;
    }

    @Override
    public List<Ledger> findAllByIsActive() {
        return ledgerRepository.findAllByActive(true);
    }

    @Override
    public Ledger save(Ledger ledger, String identityInfo) {
        ledger.setInitData();
        Ledger ledger1 = ledgerRepository.customSave(ledger, identityInfo);
        ledger1.setLedgerGroup(ledger.getLedgerGroup());
        ledger1.setSociety(ledger.getSociety());
        return ledger1;
    }

    @Override
    public Ledger update(Ledger ledger, String identityInfo) {
        ledger.setupdateData();
        Ledger ledger1 = ledgerRepository.customUpdate(ledger, identityInfo);
        ledger1.setLedgerGroup(ledger.getLedgerGroup());
        return ledger1;
    }

    @Override
    public Optional<Ledger> findById(String ledgerNo) {
        return ledgerRepository.findById(ledgerNo);
    }


    @Override
    public void delete(String ledgerNo, String identityInfo) {
        Ledger ledger = ledgerRepository.findById(ledgerNo).orElseThrow();
        ledgerRepository.customDelete(ledger, identityInfo);

    }

    @Override
    public void delete(Ledger ledger, String identityInfo) {
        ledgerRepository.customDelete(ledger, identityInfo);

    }

    @Override
    public LedgerSubLedgerMapping save(List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList, String identityInfo) {

        for (LedgerSubLedgerMapping mappingLedgerSubLedgerMapping : ledgerSubLedgerMappingList) {
            mappingLedgerSubLedgerMapping.setInitData();
            mappingLedgerSubLedgerMapping.setSociety(Hibernate.unproxy(mappingLedgerSubLedgerMapping.getSociety(), Society.class));
            mappingLedgerSubLedgerMapping.setCode(mappingLedgerSubLedgerMapping.getSubLedger().getCode() + "-" + mappingLedgerSubLedgerMapping.getLedger().getCode());
            ledgerSubLedgerMappingRepository.customSave(mappingLedgerSubLedgerMapping, identityInfo);
        }
        return null;
    }
}