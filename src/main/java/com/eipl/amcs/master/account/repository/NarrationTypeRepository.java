package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.NarrationType;
import org.springframework.stereotype.Repository;

@Repository
public interface NarrationTypeRepository extends BaseRepository<NarrationType, String> {
}