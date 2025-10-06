package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerMappingEvent;
import com.eipl.amcs.master.account.repository.LedgerMappingEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class LedgerMappingEventServiceImpl implements LedgerMappingEventService {

    @Autowired
    private LedgerMappingEventRepository ledgerMappingEventRepository;

    private static final Logger log = LoggerFactory.getLogger(LedgerMappingEventServiceImpl.class);


    @Override
    public List<LedgerMappingEvent> findAll() {
        List<LedgerMappingEvent> list = ledgerMappingEventRepository.findAll(Sort.by("code"));
        log.info("LedgerTypes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public String save(List<LedgerMappingEvent> ledgerMappingEvent, String identityInfo) {
        List<LedgerMappingEvent> list = ledgerMappingEventRepository.findAll(Sort.by("code"));

        String societyCode = new String(Base64.getDecoder().decode(identityInfo)).split("#")[1];
        for (LedgerMappingEvent mappingEvent : ledgerMappingEvent) {
            Optional<LedgerMappingEvent> obj = list.stream().filter(p -> p.getEvents().getCode().toString().equalsIgnoreCase(mappingEvent.getEvents().getCode().toString()))
                    .findFirst();
            if (obj.isPresent()) {
                LedgerMappingEvent ooo = obj.get();
                ooo.setupdateData();
                ooo.setEvents(mappingEvent.getEvents());
                if (ooo.getCreditLedger() != null) {
                    ooo.setCreditLedger(mappingEvent.getCreditLedger().getCode().equalsIgnoreCase("0") ? null : mappingEvent.getCreditLedger());
                } else {
                    ooo.setCreditLedger(null);
                }
                if (ooo.getDebitLedger() != null) {
                    ooo.setDebitLedger(mappingEvent.getDebitLedger().getCode().equalsIgnoreCase("0") ? null : mappingEvent.getDebitLedger());
                } else {
                    ooo.setDebitLedger(null);
                }
                ooo.setDebitSubLedger(mappingEvent.getDebitSubLedger());
                ooo.setCreditSubLedger(mappingEvent.getCreditSubLedger());
                ooo.setVoucherType(mappingEvent.getVoucherType());
                ooo.setxCol1(mappingEvent.getxCol1());
                ledgerMappingEventRepository.customUpdate(ooo, identityInfo);
            } else {
                mappingEvent.setInitData();
                mappingEvent.setCreditLedger(mappingEvent.getCreditLedger().getCode().
                        equalsIgnoreCase("0") ? null : mappingEvent.getCreditLedger());
                mappingEvent.setDebitLedger(mappingEvent.getDebitLedger().getCode().
                        equalsIgnoreCase("0") ? null : mappingEvent.getDebitLedger());
                mappingEvent.setCode(mappingEvent.getEvents().getCode());
                mappingEvent.setEventcode(Integer.parseInt(mappingEvent.getSociety().getCode() + mappingEvent.getEvents().getEventCode()));
                ledgerMappingEventRepository.customSave(mappingEvent, identityInfo);
            }
        }

        return "OK";
    }

    @Override
    public Optional<LedgerMappingEvent> findById(String ledgerTypeNo) {
        return Optional.empty();
    }

}