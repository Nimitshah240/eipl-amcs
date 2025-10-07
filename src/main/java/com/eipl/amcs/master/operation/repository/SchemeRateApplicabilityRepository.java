package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.master.operation.model.SchemeRateApplicability;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface SchemeRateApplicabilityRepository extends JpaRepository<SchemeRateApplicability, Integer> {
    @Transactional
    void deleteBySchemeRateAppCode(Integer schemeRateAppCode);

    List<SchemeRateApplicability> findByIsActiveTrue();
}
