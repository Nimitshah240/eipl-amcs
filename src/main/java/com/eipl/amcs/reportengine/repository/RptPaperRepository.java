package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RptPaperRepository extends JpaRepository<RptPaper, Long> {
}
