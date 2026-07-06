package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.ReportRequest;
import com.eipl.amcs.reportengine.dto.ReportResult;

public interface ReportExecutionService {

    ReportResult execute(ReportRequest request);

}