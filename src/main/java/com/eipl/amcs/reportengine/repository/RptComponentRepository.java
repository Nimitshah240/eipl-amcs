package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RptComponentRepository extends JpaRepository<RptComponent, Long> {
    List<RptComponent> findBySectionSectionId(Long sectionId);
}