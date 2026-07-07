package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptDatasource;
import com.eipl.amcs.reportengine.model.RptDatasourceParameter;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.repository.RptDatasourceParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ParameterResolver parameterResolver;

    @Override
    @Transactional
    public ReportResult execute(RptDatasource datasource, Map<String, Object> parameters) {

        List<RptDatasourceParameter> dsParameters = datasourceParameterRepository.findByDatasourceDatasourceCodeOrderByParameterOrder(datasource.getDatasourceCode());

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(datasource.getDefinition());

        int index = 1;

        for (RptDatasourceParameter dsParameter : dsParameters) {

            RptParameterMaster parameter = dsParameter.getParameterMaster();
            Object selectedValue = null;
            String parameterCode = null;
            if (parameter == null) {
                selectedValue = dsParameter.getDefaultValue();
            } else {
                parameterCode = parameter.getParameterMasterCode();
                selectedValue = parameters.get(parameterCode);
            }

            Class<?> parameterClass = determineTypeClass(dsParameter.getDataType());
            Object value = parameterResolver.resolve(parameter, selectedValue);

            query.registerStoredProcedureParameter(index, parameterClass, ParameterMode.IN);
            System.out.println(value);
            query.setParameter(index, value);

            index++;
        }

        query.execute();

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();

        List<Map<String, Object>> rows = new ArrayList<>();

        for (Object[] row : resultList) {

            Map<String, Object> map = new LinkedHashMap<>();

            for (int i = 0; i < row.length; i++) {

                map.put("COLUMN_" + (i + 1), row[i]);

            }

            rows.add(map);
        }

        ReportResult result = new ReportResult();
        result.setData(rows);

        return result;
    }

    private Class<?> determineTypeClass(String dataType) {
        if (dataType == null) {
            return String.class; // default fallback safety assignment
        }

        switch (dataType.toUpperCase()) {
            case "INT":
            case "INTEGER":
                return Integer.class;
            case "BIGINT":
            case "LONG":
                return Long.class;
            case "DATE":
            case "DATETIME":
                return java.sql.Date.class; // or java.time.LocalDate.class / java.util.Date.class depending on JDBC configuration
            case "BOOLEAN":
                return Boolean.class;
            case "STRING":
            case "VARCHAR":
            default:
                return String.class;
        }
    }
}