package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptDatasourceParameter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RptDatasourceParameterRepository extends JpaRepository<RptDatasourceParameter, Long> {

    @EntityGraph(attributePaths = {"parameterMaster", "parameterMaster.lookup", "datasource"})
    List<RptDatasourceParameter> findByDatasourceDatasourceCodeOrderByParameterOrder(Long datasourceCode);

}