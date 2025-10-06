package com.eipl.amcs.setting.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.setting.model.GeneralConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneralConfigRepository extends BaseRepository<GeneralConfig, String> {

    @Override
    List<GeneralConfig> findAll();

    @Override
    Optional<GeneralConfig> findById(String code);

}
