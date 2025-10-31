package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.VoucherTypeLedgerConfig;
import com.eipl.amcs.master.account.repository.VoucherTypeLedgerConfigRepository;
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

    private static final Logger log = LoggerFactory.getLogger(VoucherTypeLedgerConfigServiceImpl.class);
    @Autowired
    private VoucherTypeLedgerConfigRepository voucherTypeLedgerConfigRepository;

    @Override
    public List<VoucherTypeLedgerConfig> findAll() {
        List<VoucherTypeLedgerConfig> list = voucherTypeLedgerConfigRepository.findAll(Sort.by("code"));
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
                } else {
                    if (mappingVoucherType.getLedger() != null) {
                        mappingVoucherType.setInitData();
                        mappingVoucherType.setLedger(mappingVoucherType.getLedger().getCode().
                                equalsIgnoreCase("0") ? null : mappingVoucherType.getLedger());
                        mappingVoucherType.setCode(societyCode + "-" + mappingVoucherType.getVoucherType().getCode());
                        voucherTypeLedgerConfigRepository.customSave(mappingVoucherType, identityInfo);
                    }
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