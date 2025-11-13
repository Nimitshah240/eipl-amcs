package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.account.repository.LedgerMappingProductGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Service
public class LedgerMappingProductGroupServiceImpl implements LedgerMappingProductGroupService {

    private static final Logger log = LoggerFactory.getLogger(LedgerMappingProductGroupServiceImpl.class);
    @Autowired
    private LedgerMappingProductGroupRepository ledgerMappingProductGroupRepository;

    @Override
    public List<LedgerMappingProductGroup> findAll() {
        List<LedgerMappingProductGroup> list = ledgerMappingProductGroupRepository.findAll(Sort.by("productGroup"));
        log.info("LedgerTypes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public String save(List<LedgerMappingProductGroup> ledgerMappingProductGroup, String identityInfo) {
        List<LedgerMappingProductGroup> list = ledgerMappingProductGroupRepository.findAll(Sort.by("code"));

        String societyCode = new String(Base64.getDecoder().decode(identityInfo)).split("#")[1];
        for (LedgerMappingProductGroup mappingProductGroup : ledgerMappingProductGroup) {
            if (mappingProductGroup.getLedgerSaleCode() != null) {
                Optional<LedgerMappingProductGroup> obj = list.stream().filter(p -> p.getProductGroup().getCode().toString().equalsIgnoreCase(mappingProductGroup.getProductGroup().getCode().toString()))
                        .findFirst();
                if (obj.isPresent()) {
                    LedgerMappingProductGroup ooo = obj.get();
                    ooo.setupdateData();
                    ooo.setProductGroup(mappingProductGroup.getProductGroup());
                    if (ooo.getLedgerPurchaseCode() != null && mappingProductGroup.getLedgerPurchaseCode() != null) {
                        ooo.setLedgerPurchaseCode(mappingProductGroup.getLedgerPurchaseCode().getCode().
                                equalsIgnoreCase("0") ? null : mappingProductGroup.getLedgerPurchaseCode());
                    } else {
                        ooo.setLedgerPurchaseCode(null);
                    }
                    if (ooo.getLedgerSaleCode() != null) {
                        ooo.setLedgerSaleCode(mappingProductGroup.getLedgerSaleCode().getCode().
                                equalsIgnoreCase("0") ? null : mappingProductGroup.getLedgerSaleCode());
                    } else {
                        ooo.setLedgerSaleCode(null);
                    }
                    ooo.setSociety(mappingProductGroup.getSociety());
                    ooo.setUnionCode(mappingProductGroup.getUnionCode());
                    ledgerMappingProductGroupRepository.customUpdate(ooo, identityInfo);
                } else {
                    mappingProductGroup.setInitData();
                    if (mappingProductGroup.getLedgerPurchaseCode() != null)
                        mappingProductGroup.setLedgerPurchaseCode(mappingProductGroup.getLedgerPurchaseCode().getCode().
                                equalsIgnoreCase("0") ? null : mappingProductGroup.getLedgerPurchaseCode());
                    mappingProductGroup.setLedgerSaleCode(mappingProductGroup.getLedgerSaleCode().getCode().
                            equalsIgnoreCase("0") ? null : mappingProductGroup.getLedgerSaleCode());
                    if (mappingProductGroup.getLedgerSaleCode() == null && mappingProductGroup.getLedgerPurchaseCode() == null)
                        continue;
                    mappingProductGroup.setCode(societyCode + "-" + mappingProductGroup.getProductGroup().getCode());
                    ledgerMappingProductGroupRepository.customSave(mappingProductGroup, identityInfo);
                }
            } else {
                if (mappingProductGroup.getCode() != null)
                    ledgerMappingProductGroupRepository.customDelete(mappingProductGroup, identityInfo);
            }
        }

        return "OK";
    }

    @Override
    public Optional<LedgerMappingProductGroup> findById(String ledgerTypeNo) {
        return Optional.empty();
    }

}