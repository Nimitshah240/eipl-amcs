package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.VoucherTypeLedgerConfig;
import com.eipl.amcs.master.account.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Service
public class VoucherTypeLedgerConfigServiceImpl implements VoucherTypeLedgerConfigService {

    @Autowired
    private VoucherTypeLedgerConfigRepository voucherTypeLedgerConfigRepository;

    private static final Logger log = LoggerFactory.getLogger(VoucherTypeLedgerConfigServiceImpl.class);

    @Override
    public List<VoucherTypeLedgerConfig> findAll() {
        List<VoucherTypeLedgerConfig> list = voucherTypeLedgerConfigRepository.findAll(Sort.by("code"));
        log.info("LedgerTypes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public String save(List<VoucherTypeLedgerConfig> ledgerMappingProductGroup, String identityInfo) {
        List<VoucherTypeLedgerConfig> list = voucherTypeLedgerConfigRepository.findAll(Sort.by("code"));

        String societyCode = new String(Base64.getDecoder().decode(identityInfo)).split("#")[1];
        for (VoucherTypeLedgerConfig mappingVoucherType : ledgerMappingProductGroup) {
            if (mappingVoucherType.getVoucherType() != null) {
                Optional<VoucherTypeLedgerConfig> obj = list.stream().filter(p -> p.getVoucherType().getCode().toString().equalsIgnoreCase(mappingVoucherType.getVoucherType().getCode().toString()))
                        .findFirst();
                if (obj.isPresent()) {
                    VoucherTypeLedgerConfig ooo = obj.get();
                    if (ooo.getLedger() != null) {
                        ooo.setLedger(mappingVoucherType.getLedger().getCode().equalsIgnoreCase("0") ? null : mappingVoucherType.getLedger());
                    } else {
                        ooo.setLedger(null);
                    }
                    ooo.setVoucherType(mappingVoucherType.getVoucherType());
                    ooo.setCreditDebit(mappingVoucherType.getCreditDebit());
                    ooo.setupdateData();
                    if (!mappingVoucherType.getLedger().getCode().equalsIgnoreCase("0"))
                        voucherTypeLedgerConfigRepository.customUpdate(mappingVoucherType, identityInfo);
                    else {
                    }
//					voucherTypeLedgerConfigRepository.save(ooo);
                } else {
                    if (mappingVoucherType.getLedger() != null) {
                        mappingVoucherType.setInitData();
                        mappingVoucherType.setLedger(mappingVoucherType.getLedger().getCode().
                                equalsIgnoreCase("0") ? null : mappingVoucherType.getLedger());
                        mappingVoucherType.setCode(societyCode + "-" + mappingVoucherType.getVoucherType().getCode());
                        voucherTypeLedgerConfigRepository.customSave(mappingVoucherType, identityInfo);
                    }
//					voucherTypeLedgerConfigRepository.save(mappingVoucherType);
                }
            }
        }

        return "OK";
    }

    @Override
    public Optional<VoucherTypeLedgerConfig> findById(String ledgerTypeNo) {
        return Optional.empty();
    }

}