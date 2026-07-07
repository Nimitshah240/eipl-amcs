package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RptSectionRepository extends JpaRepository<RptSection, Long> {
    List<RptSection> findByReportReportCodeOrderByDisplayOrderAsc(Long reportId);
}
