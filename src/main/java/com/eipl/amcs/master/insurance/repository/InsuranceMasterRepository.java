package com.eipl.amcs.master.insurance.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceMasterRepository extends BaseRepository<InsuranceMaster, Integer> {

    @Override
    List<InsuranceMaster> findAll(Sort sort);
}
