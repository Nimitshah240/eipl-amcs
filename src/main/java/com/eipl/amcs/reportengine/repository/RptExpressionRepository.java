package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RptExpressionRepository extends JpaRepository<RptExpression, Long> {
    List<RptExpression> findByComponentComponentId(Long componentId);
}
