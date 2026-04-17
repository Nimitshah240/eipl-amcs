package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import com.eipl.amcs.master.account.repository.LedgerMappingTaxDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class LedgerMappingTaxDetailServiceImpl implements LedgerMappingTaxDetailService {

    private static final Logger log = LoggerFactory.getLogger(LedgerMappingTaxDetailServiceImpl.class);
    @Autowired
    private LedgerMappingTaxDetailRepository ledgerMappingTaxDetailRepository;

    @Override
    public List<LedgerMappingTaxDetail> findAll() {
        List<LedgerMappingTaxDetail> list = ledgerMappingTaxDetailRepository.findAll(Sort.by("code"));
        log.info("LedgerTypes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public String save(List<LedgerMappingTaxDetail> ledgerMappingTaxDetail, String identityInfo) {
        List<LedgerMappingTaxDetail> list = ledgerMappingTaxDetailRepository.findAll(Sort.by("code"));

        String societyCode = new String(Base64.getDecoder().decode(identityInfo)).split("#")[1];
        for (LedgerMappingTaxDetail mappingTaxDetail : ledgerMappingTaxDetail) {
            if (mappingTaxDetail.getPurchaseLedger() != null) {
                Optional<LedgerMappingTaxDetail> obj = list.stream().filter(p -> p.getTaxDetail().getCode().equalsIgnoreCase(mappingTaxDetail.getTaxDetail().getCode()))
                        .findFirst();
                if (obj.isPresent()) {
                    LedgerMappingTaxDetail ooo = obj.get();
                    ooo.setupdateData();

                    ooo.setTaxDetail(mappingTaxDetail.getTaxDetail());
                    ooo.setPurchaseLedger(mappingTaxDetail.getPurchaseLedger());
                    ooo.setSaleLedger(mappingTaxDetail.getSaleLedger());
                    ooo.setSociety(mappingTaxDetail.getSociety());
                    ooo.setUnionCode(mappingTaxDetail.getUnionCode());
                    ledgerMappingTaxDetailRepository.customUpdate(ooo, identityInfo);
                } else {
                    mappingTaxDetail.setInitData();
                    mappingTaxDetail.setCode(societyCode + "-" + mappingTaxDetail.getTaxDetail().getCode() + "-" + mappingTaxDetail.getPurchaseLedger().getCode());
                    mappingTaxDetail.setPurchaseLedger(mappingTaxDetail.getPurchaseLedger().getCode().equalsIgnoreCase("0") ? null : mappingTaxDetail.getPurchaseLedger());
                    mappingTaxDetail.setSaleLedger(mappingTaxDetail.getSaleLedger().getCode().equalsIgnoreCase("0") ? null : mappingTaxDetail.getSaleLedger());
                    ledgerMappingTaxDetailRepository.customSave(mappingTaxDetail, identityInfo);
                }
            } else {
                ledgerMappingTaxDetailRepository.customDelete(mappingTaxDetail, identityInfo);
            }
        }

        return "OK";
    }

    @Override
    public Optional<LedgerMappingTaxDetail> findById(String ledgerTypeNo) {
        return Optional.empty();
    }

}