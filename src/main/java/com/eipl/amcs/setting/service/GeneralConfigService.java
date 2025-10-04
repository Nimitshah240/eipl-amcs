package com.eipl.amcs.setting.service;

import com.eipl.amcs.setting.model.GeneralConfig;

import java.util.List;
import java.util.Optional;

public interface GeneralConfigService {

    List<GeneralConfig> findAll();

    List<GeneralConfig> save(List<GeneralConfig> object, String identityInfo);

    GeneralConfig update(GeneralConfig object, String identityInfo);

    Optional<GeneralConfig> findById(String object);

    void delete(String code, String identityInfo);

    void delete(GeneralConfig object, String identityInfo);

}
