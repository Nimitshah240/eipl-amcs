package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptTableResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RptTableResultRepository extends JpaRepository<RptTableResult, Long> {
    List<RptTableResult> findByReportCode(Long reportCode, Sort sort);
}