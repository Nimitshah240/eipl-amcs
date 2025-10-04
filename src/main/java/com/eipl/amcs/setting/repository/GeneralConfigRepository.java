package com.eipl.amcs.setting.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.setting.model.GeneralConfig;

import java.util.List;
import java.util.Optional;

public interface GeneralConfigRepository extends BaseRepository<GeneralConfig, String> {

    @Override
    List<GeneralConfig> findAll();

    @Override
    Optional<GeneralConfig> findById(String code);

}
