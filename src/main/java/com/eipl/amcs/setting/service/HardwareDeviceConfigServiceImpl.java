package com.eipl.amcs.setting.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.setting.repository.HardwareDeviceConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HardwareDeviceConfigServiceImpl implements HardwareDeviceConfigService {

    @Autowired
    private HardwareDeviceConfigRepository hardwareRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;

    @Override
    public List<HardwareDeviceConfig> findAll() {
        return hardwareRepository.findAll();
    }

    @Override
    @Transactional
    public String saveUpdate(List<HardwareDeviceConfig> list, String identityInfo) {
//        List<HardwareDeviceConfig> listPrev = hardwareRepository.findAll();
        List<HardwareDeviceConfig> listPrev = hardwareRepository.findByDock(list.get(0).getDock());

        String nextCode = nextCodeRepository.getNextCode("HardwareDeviceConfig", "code",
                list.get(0).getSociety().getCode(), 0);

        for (HardwareDeviceConfig item : list) {
            item.setCode(nextCode);
            item.setInitData();
            hardwareRepository.customSave(item, identityInfo);

            // next code
            nextCode = list.get(0).getSociety().getCode() + (Integer.parseInt(nextCode.replace(list.get(0).getSociety().getCode(), "")) + 1);
        }

        listPrev.forEach(item -> {
            hardwareRepository.delete(item);
        });
        return "Hardware config saved successfully";
    }

    @Override
    public HardwareDeviceConfig save(HardwareDeviceConfig hardwareDeviceConfig, String identityInfo) {
        return hardwareRepository.customSave(hardwareDeviceConfig, identityInfo);
    }

    @Override
    public HardwareDeviceConfig update(HardwareDeviceConfig hardwareDeviceConfig, String identityInfo) {
        return hardwareRepository.customUpdate(hardwareDeviceConfig, identityInfo);
    }

    @Override
    public Optional<HardwareDeviceConfig> findById(String code) {
        return hardwareRepository.findById(code);
    }

    @Override
    public void delete(String code, String identityInfo) {
        hardwareRepository.customDelete(hardwareRepository.findById(code).get(), identityInfo);
    }

    @Override
    public void delete(HardwareDeviceConfig hardwareDeviceConfig, String identityInfo) {
        hardwareRepository.customDelete(hardwareDeviceConfig.getCode(), identityInfo);
    }
}
