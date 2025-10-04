package com.eipl.amcs.master.insurance.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface InsuranceDetailSummaryRepository extends BaseRepository<InsuranceDetailSummary, Integer> {

    @Override
    List<InsuranceDetailSummary> findAll(Sort sort);

    InsuranceDetailSummary findByInsuranceMasterCode(Integer insuranceMasterCode);
}
