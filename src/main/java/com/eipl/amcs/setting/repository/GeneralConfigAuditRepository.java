package com.eipl.amcs.setting.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.setting.model.GeneralConfigAudit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneralConfigAuditRepository extends BaseRepository<GeneralConfigAudit, Integer> {

    @Override
    List<GeneralConfigAudit> findAll();

    @Override
    Optional<GeneralConfigAudit> findById(Integer code);

}
