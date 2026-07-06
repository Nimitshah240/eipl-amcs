package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RptGroupRepository extends JpaRepository<RptGroup, Long> {
    List<RptGroup> findByComponentComponentIdOrderByGroupLevelAsc(Long componentId);
}