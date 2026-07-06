package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptDatasource;
import com.eipl.amcs.reportengine.model.RptDatasourceParameter;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.repository.RptDatasourceParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoredProcedureExecutorImpl implements StoredProcedureExecutor {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private RptDatasourceParameterRepository datasourceParameterRepository;

    @Override
    public ReportResult execute(RptDatasource datasource,
                                Map<String, Object> parameters) {

        List<RptDatasourceParameter> dsParameters = datasourceParameterRepository.findByDatasourceDatasourceIdOrderBySequenceNo(datasource.getDatasourceId());
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(datasource.getDefinition());

        int index = 1;

        for (RptDatasourceParameter dsParameter : dsParameters) {

            RptParameterMaster parameter = dsParameter.getDatasource();

            String parameterCode = parameter.getParameterCode();

            Object value = parameters.get(parameterCode);

            query.registerStoredProcedureParameter(
                    index,
                    Object.class,
                    ParameterMode.IN
            );

            query.setParameter(index, value);

            index++;
        }

        query.execute();

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();

        ReportResult result = new ReportResult();

        List<Map<String, Object>> rows = new ArrayList<>();

        for (Object[] row : resultList) {

            Map<String, Object> map = new LinkedHashMap<>();

            for (int i = 0; i < row.length; i++) {

                map.put("COLUMN_" + (i + 1), row[i]);

            }

            rows.add(map);
        }

        result.setData(rows);

        return result;
    }
}