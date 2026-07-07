package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptLookupParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RptLookupParameterRepository extends JpaRepository<RptLookupParameter, Long> {
    List<RptLookupParameter> findByLookupLookupCode(Long lookupId);
}