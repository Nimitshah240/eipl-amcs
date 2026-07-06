package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptDatasource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatasourceExecutorImpl
        implements DatasourceExecutor {

    @Autowired
    private final StoredProcedureExecutor storedProcedureExecutor;

    @Override
    public ReportResult execute(RptDatasource datasource, Map<String, Object> parameters) {

        switch (datasource.getDatasourceType()) {

            case "SP":
                return storedProcedureExecutor.execute(datasource, parameters);

            default:
                throw new RuntimeException("Datasource not supported");
        }
    }

}