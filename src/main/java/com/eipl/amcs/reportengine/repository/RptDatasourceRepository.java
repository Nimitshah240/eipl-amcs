package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptDatasource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RptDatasourceRepository extends JpaRepository<RptDatasource, Long> {
    List<RptDatasource> findByActiveTrue();
}