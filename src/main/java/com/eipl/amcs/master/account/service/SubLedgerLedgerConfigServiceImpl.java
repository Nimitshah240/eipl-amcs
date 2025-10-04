package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import com.eipl.amcs.master.account.repository.StaffSalaryRepository;
import com.eipl.amcs.master.account.repository.SubLedgerLedgerConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.subLedgerLedgerConfigRepository;

@Service
public class SubLedgerLedgerConfigServiceImpl implements SubLedgerLedgerConfigService {

//    private SubLedgerLedgerConfigRepository subLedgerLedgerConfigRepository;

    private static final Logger log = LoggerFactory.getLogger(SubLedgerLedgerConfigServiceImpl.class);


    @Override
    public List<SubLedgerLedgerConfig> findAll() {
        List<SubLedgerLedgerConfig> list = subLedgerLedgerConfigRepository.findAll(Sort.by("code"));
        log.info("LedgerTypes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public String save(List<SubLedgerLedgerConfig> subLedgerLedgerConfig, String subLedgerTypeCode, String identityInfo) {
        String societyCode = new String(Base64.getDecoder().decode(identityInfo)).split("#")[1];
        if (subLedgerLedgerConfig == null || subLedgerLedgerConfig.isEmpty()) {
            subLedgerLedgerConfigRepository.deleteAll(subLedgerLedgerConfigRepository.findBySubLedgerType(Integer.parseInt(subLedgerTypeCode)));
        } else {
            List<SubLedgerLedgerConfig> existingList = subLedgerLedgerConfigRepository
                    .findBySubLedgerType(subLedgerLedgerConfig.get(0).getSubLedgerType());
            if (existingList != null) {
                for (SubLedgerLedgerConfig ledgerLedgerConfig : existingList) {
                    try {
                        if (subLedgerLedgerConfig.stream().filter(e -> e.getCode() != null).collect(Collectors.toList()).stream().
                                filter(e -> e.getCode().equalsIgnoreCase(ledgerLedgerConfig.getCode())).findAny().isEmpty()) {
                            subLedgerLedgerConfigRepository.customDelete(ledgerLedgerConfig, identityInfo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            for (SubLedgerLedgerConfig mappingProductGroup : subLedgerLedgerConfig) {
                if (mappingProductGroup.getLedger() != null) {
                    mappingProductGroup.setInitData();
                    mappingProductGroup.setCode(societyCode + "-" + mappingProductGroup.getSubLedgerType() + "-"
                            + mappingProductGroup.getLedger().getCode());
//                    subLedgerLedgerConfigRepository.customSave(mappingProductGroup, identityInfo);
                    subLedgerLedgerConfigRepository.customSave(mappingProductGroup, identityInfo);
                } else {
                    subLedgerLedgerConfigRepository.customDelete(mappingProductGroup, identityInfo);
                }
            }
        }
        return "OK";
    }

    @Override
    public Optional<SubLedgerLedgerConfig> findById(String ledgerTypeNo) {
        return Optional.empty();
    }

    @Override
    public List<SubLedgerLedgerConfig> findBySubLedgerType(Integer code) {
        // TODO Auto-generated method stub
        return subLedgerLedgerConfigRepository.findBySubLedgerType(code);
    }

}