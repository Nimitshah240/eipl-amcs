package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RptStyleRepository extends JpaRepository<RptStyle, Long> {
}
