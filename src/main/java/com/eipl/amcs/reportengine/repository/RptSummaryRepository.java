package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RptSummaryRepository extends JpaRepository<RptSummary, Long> {
    List<RptSummary> findByComponentComponentId(Long componentId);
}