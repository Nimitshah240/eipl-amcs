package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptGridColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RptGridColumnRepository extends JpaRepository<RptGridColumn, Long> {
    List<RptGridColumn> findByComponentComponentIdOrderByDisplayOrderAsc(Long componentId);
}