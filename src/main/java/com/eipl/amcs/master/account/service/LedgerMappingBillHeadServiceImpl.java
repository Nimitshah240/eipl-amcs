package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerMappingBillHead;
import com.eipl.amcs.master.account.repository.LedgerGroupRepository;
import com.eipl.amcs.master.account.repository.LedgerMappingBillHeadRepository;
import com.eipl.amcs.master.account.repository.LedgerTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.ledgerMappingBillHeadRepository;

@Service
public class LedgerMappingBillHeadServiceImpl implements LedgerMappingBillHeadService {

//    private LedgerMappingBillHeadRepository ledgerMappingBillHeadRepository;

    private static final Logger log = LoggerFactory.getLogger(LedgerMappingBillHeadServiceImpl.class);


    @Override
    public List<LedgerMappingBillHead> findAll() {
        List<LedgerMappingBillHead> list = ledgerMappingBillHeadRepository.findAll(Sort.by("code"));
        log.info("LedgerTypes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public String save(List<LedgerMappingBillHead> ledgerMappingBillHead, String identityInfo) {
        List<LedgerMappingBillHead> list = ledgerMappingBillHeadRepository.findAll(Sort.by("code"));

        String societyCode = new String(Base64.getDecoder().decode(identityInfo)).split("#")[1];
        for (LedgerMappingBillHead mappingBillHead : ledgerMappingBillHead) {
            if (mappingBillHead.getLedger() != null) {
                Optional<LedgerMappingBillHead> obj = list.stream().filter(p -> p.getBillHead().getCode().equalsIgnoreCase(mappingBillHead.getBillHead().getCode()))
                        .findFirst();
                if (obj.isPresent()) {
                    LedgerMappingBillHead ooo = obj.get();
                    ooo.setupdateData();

                    ooo.setType(mappingBillHead.getType());
                    ooo.setLedger(mappingBillHead.getLedger());
                    ooo.setBillHead(mappingBillHead.getBillHead());
                    ooo.setBillCriteria(mappingBillHead.getBillCriteria());
                    ooo.setHasSubLedger(mappingBillHead.getHasSubLedger());
                    ooo.setSociety(mappingBillHead.getSociety());
                    ooo.setUnionCode(mappingBillHead.getUnionCode());
                    ooo.setCreditDebit(mappingBillHead.getCreditDebit());
                    ooo.setxCol1(mappingBillHead.getXCol1());
                    ledgerMappingBillHeadRepository.customUpdate(ooo, identityInfo);
                } else {
                    mappingBillHead.setInitData();
                    mappingBillHead.setCode(societyCode + "-" + mappingBillHead.getBillHead().getCode());
                    mappingBillHead.setLedger(mappingBillHead.getLedger().getCode().equalsIgnoreCase("0") ? null : mappingBillHead.getLedger());
                    ledgerMappingBillHeadRepository.customSave(mappingBillHead, identityInfo);
                }
            }
        }

        return "OK";
    }

    @Override
    public Optional<LedgerMappingBillHead> findById(String ledgerTypeNo) {
        return Optional.empty();
    }

}