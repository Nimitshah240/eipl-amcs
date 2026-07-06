package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptReport;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RptReportRepository extends JpaRepository<RptReport, Long> {

    @EntityGraph(attributePaths = {"datasource", "layout"})
    Optional<RptReport> findByReportCode(Long reportCode);

    @EntityGraph(attributePaths = {"datasource", "layout"})
    Optional<RptReport> findById(Long reportCode);
}