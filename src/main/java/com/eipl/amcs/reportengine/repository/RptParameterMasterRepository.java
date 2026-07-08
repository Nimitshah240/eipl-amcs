package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptParameterMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RptParameterMasterRepository extends JpaRepository<RptParameterMaster, String> {
}