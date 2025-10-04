package com.eipl.amcs.master.insurance.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface InsuranceDetailRepository extends BaseRepository<InsuranceDetail, String> {

    @Override
    List<InsuranceDetail> findAll(Sort sort);

    List<InsuranceDetail> findByInsuranceMasterCode(Integer insuranceMasterCode);

    List<InsuranceDetail> findByInsuranceMasterCodeAndIsDelete(Integer insuranceMasterCode, boolean b, Sort sort);
}
