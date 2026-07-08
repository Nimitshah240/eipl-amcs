package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RptExportRepository extends JpaRepository<RptExport, Long> {
}
