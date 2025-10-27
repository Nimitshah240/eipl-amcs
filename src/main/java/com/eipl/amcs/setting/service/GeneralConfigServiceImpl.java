package com.eipl.amcs.setting.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.setting.model.GeneralConfig;
import com.eipl.amcs.setting.model.GeneralConfigAudit;
import com.eipl.amcs.setting.repository.GeneralConfigAuditRepository;
import com.eipl.amcs.setting.repository.GeneralConfigRepository;
import com.eipl.amcs.sync.model.Broadcasted;
import com.eipl.amcs.sync.repository.BroadcastedRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class GeneralConfigServiceImpl implements GeneralConfigService {
    @Autowired
    private GeneralConfigRepository generalConfigRepository;
    @Autowired
    private GeneralConfigAuditRepository auditRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private BroadcastedRepository broadcastedRepository;
    @Autowired
    private SocietyRepository societyRepository;

    //    private final EntityManager entityManager;
    private final ObjectMapper objMapper;

    //
    public GeneralConfigServiceImpl() {
        objMapper = Jackson2ObjectMapperBuilder.json().build();
    }

    @Override
    public List<GeneralConfig> findAll() {
        List<GeneralConfig> list = generalConfigRepository.findAll();
        return list;
    }


    @Override
    @Transactional
    public List<GeneralConfig> save(List<GeneralConfig> list, String identityInfo) {
        try {
            String soc = list.stream().filter(e -> e.getKey().equalsIgnoreCase("identity.society")).findAny().get().getValue();
            Society society = societyRepository.findById(soc).get();
            String code = "";
            if (list.get(0).getSociety() != null)
                code = nextCodeService.getNextCode("GeneralConfig", "code", list.get(0).getSociety().getCode(), 0);
            else {
                code = nextCodeService.getNextCode("GeneralConfig", "code", soc, 0);
            }
            long c = Long.parseLong(code);
            List<GeneralConfig> generalConfigList = new ArrayList<>(generalConfigRepository.findAll());
            Broadcasted broadcast;
            String json;

            generalConfigRepository.deleteAll();
            for (GeneralConfig generalConfig : list) {
                try {
                    generalConfig.setSociety(society);
                    generalConfig.setInitData();
                    generalConfig.setCode(String.valueOf(c++));

//                    json = objMapper.writeValueAsString(generalConfig);
                    generalConfigRepository.save(generalConfig);
//
//                    String[] val = new String(Base64.getDecoder().decode(identityInfo)).split("#");
//                    broadcast = Broadcasted.prepareBroadcaste(json, val[0], val[1], generalConfig.getTableName(), "INSERT", val[2], val[3], val[4]);
//                    broadcastedRepository.save(broadcast);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!generalConfigList.isEmpty()) {
                for (GeneralConfig generalConfig : generalConfigList) {
                    GeneralConfigAudit audit = (GeneralConfigAudit) generalConfig.getAuditModel();
                    auditRepository.save(audit);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public GeneralConfig update(GeneralConfig objectConfig, String identityInfo) {

        objectConfig.setupdateData();
        return generalConfigRepository.customUpdate(objectConfig, identityInfo);

    }

    @Override
    public Optional<GeneralConfig> findById(String code) {
        return generalConfigRepository.findById(code);
    }

    @Override
    public void delete(String code, String identityInfo) {
        generalConfigRepository.customDelete(generalConfigRepository.findById(code).get(), identityInfo);
    }

    @Override
    public void delete(GeneralConfig objectConfig, String identityInfo) {
        generalConfigRepository.customDelete(objectConfig.getCode(), identityInfo);
    }


//    @Transactional
//    public GeneralConfig customSaveConfig(GeneralConfig entity, String identityInfo) {
//        List<GeneralConfig> list = generalConfigRepository.findAll();
//        if (list != null) {
//            for (GeneralConfig generalConfig : list) {
//                entityManager.persist(generalConfig.getAuditModel("UPDATE", entity.getUserInfo()));
//            }
//        }
//        GeneralConfig entityNew = generalConfigRepository.save(entity);
//        if (EiplAmcsAppRunner.books.containsKey(entityNew.getTableName())) {
//            String json;
//            try {
//                json = objMapper.writeValueAsString(entity);
//                Broadcasted broadcast;
//                if (identityInfo == null || identityInfo.isEmpty()) {
//                    broadcast = Broadcasted.prepareBroadcaste(json, "SOCIETY", null, entity.getTableName(), "UPDATE",
//                            null, null, null);
//                } else {
//                    String[] val = new String(Base64.getDecoder().decode(identityInfo)).split("#");
//                    broadcast = Broadcasted.prepareBroadcaste(json, val[0], val[1], entity.getTableName(), "UPDATE",
//                            val[2], val[3], val[4]);
//                }
//                entityManager.persist(broadcast);
//            } catch (JsonProcessingException e) {
//            }
//        }
//        return entityNew;
//    }


}
