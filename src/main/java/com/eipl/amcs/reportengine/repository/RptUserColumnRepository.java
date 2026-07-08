package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptUserColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RptUserColumnRepository extends JpaRepository<RptUserColumn, Long> {
    List<RptUserColumn> findByUserIdAndReportIdOrderByDisplayOrderAsc(Long userId, Long reportId);
}