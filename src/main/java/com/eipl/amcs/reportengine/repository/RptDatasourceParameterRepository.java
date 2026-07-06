package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptDatasourceParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RptDatasourceParameterRepository extends JpaRepository<RptDatasourceParameter, Long> {
    List<RptDatasourceParameter> findByDatasourceDatasourceIdOrderBySequenceNo(Long datasourceId);
}