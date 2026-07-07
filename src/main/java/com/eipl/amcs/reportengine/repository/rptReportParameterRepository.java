package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptReportParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface rptReportParameterRepository extends JpaRepository<RptReportParameter, Long> {
}