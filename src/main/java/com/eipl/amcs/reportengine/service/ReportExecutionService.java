package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.ReportRequest;
import com.eipl.amcs.reportengine.dto.ReportResult;

import java.util.Map;

public interface ReportExecutionService {

    ReportResult execute(ReportRequest request);

    ReportResult execute(Long reportId, Map<String, Object> parameters);

}