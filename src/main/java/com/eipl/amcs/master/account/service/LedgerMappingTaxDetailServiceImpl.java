package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import com.eipl.amcs.master.account.repository.LedgerMappingProductGroupRepository;
import com.eipl.amcs.master.account.repository.LedgerMappingTaxDetailRepository;
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
import static com.eipl.amcs.config.BeanConfig.ledgerMappingTaxDetailRepository;

@Service
public class LedgerMappingTaxDetailServiceImpl implements LedgerMappingTaxDetailService {

//    private LedgerMappingTaxDetailRepository ledgerMappingTaxDetailRepository;

    private static final Logger log = LoggerFactory.getLogger(LedgerMappingTaxDetailServiceImpl.class);


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
            if (mappingTaxDetail.getLedger() != null) {
                Optional<LedgerMappingTaxDetail> obj = list.stream().filter(p -> p.getTaxDetail().getCode().equalsIgnoreCase(mappingTaxDetail.getTaxDetail().getCode()))
                        .findFirst();
                if (obj.isPresent()) {
                    LedgerMappingTaxDetail ooo = obj.get();
                    ooo.setupdateData();

                    ooo.setTaxDetail(mappingTaxDetail.getTaxDetail());
                    ooo.setLedger(mappingTaxDetail.getLedger());
                    ooo.setSociety(mappingTaxDetail.getSociety());
                    ooo.setUnionCode(mappingTaxDetail.getUnionCode());
                    ledgerMappingTaxDetailRepository.customUpdate(ooo, identityInfo);
                } else {
                    mappingTaxDetail.setInitData();
                    mappingTaxDetail.setCode(societyCode + "-" + mappingTaxDetail.getTaxDetail().getCode() + "-" + mappingTaxDetail.getLedger().getCode());
                    mappingTaxDetail.setLedger(mappingTaxDetail.getLedger().getCode().equalsIgnoreCase("0") ? null : mappingTaxDetail.getLedger());
                    ledgerMappingTaxDetailRepository.customSave(mappingTaxDetail, identityInfo);
                }
            }
        }

        return "OK";
    }

    @Override
    public Optional<LedgerMappingTaxDetail> findById(String ledgerTypeNo) {
        return Optional.empty();
    }

}