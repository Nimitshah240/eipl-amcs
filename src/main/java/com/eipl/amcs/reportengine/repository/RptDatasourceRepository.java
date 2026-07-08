package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptDatasource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RptDatasourceRepository extends JpaRepository<RptDatasource, Long> {
}