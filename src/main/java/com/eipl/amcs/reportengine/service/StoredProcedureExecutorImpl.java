package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptDatasource;
import com.eipl.amcs.reportengine.model.RptDatasourceParameter;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.model.RptTableResult;
import com.eipl.amcs.reportengine.repository.RptDatasourceParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();
        List<Map<String, Object>> rows = new ArrayList<>();

        for (Object[] obj : resultList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("society_code",obj[0]);
            map.put("society_name",obj[1]);
            map.put("member_code", obj[2]);
            map.put("member_name", obj[3]);
            map.put("period", obj[4]);
            map.put("cow_qty",obj[5]);
            map.put("cow_fat",obj[6]);
            map.put("cow_snf",obj[7]);
            map.put("cow_amount", obj[8]);
            map.put("buffalo_qty", obj[9]);
            map.put("buffalo_fat", obj[10]);
            map.put("buffalo_snf", obj[11]);
            map.put("buffalo_amount",obj[12]);
            map.put("milk_qty", obj[13]);
            map.put("milk_fat", obj[14]);
            map.put("milk_snf", obj[15]);
            map.put("milk_amount", obj[16]);
            map.put("addition_amount",obj[17]);
            map.put("deduction_amount",obj[18]);
            map.put("net_amount", obj[19]);
            map.put("bank_name", obj[20]);
            map.put("branch_name",obj[21]);
            map.put("ifsc", obj[22]);
            map.put("bank_acno",obj[23]);
            map.put("mobile_no",obj[24]);
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