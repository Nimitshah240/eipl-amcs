package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptReportParameter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RptParameterRepository extends JpaRepository<RptReportParameter, Long> {

    @EntityGraph(attributePaths = {"parameterMaster", "parameterMaster.lookup"})
    List<RptReportParameter> findByReportReportCodeOrderByDisplayOrderAsc(Long reportCode);}