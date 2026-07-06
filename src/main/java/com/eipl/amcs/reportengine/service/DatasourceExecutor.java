package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptDatasource;

import java.util.Map;

public interface DatasourceExecutor {

    ReportResult execute(RptDatasource datasource, Map<String, Object> parameters);

}