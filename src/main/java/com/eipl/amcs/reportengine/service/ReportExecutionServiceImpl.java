package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.ReportRequest;
import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptReport;
import com.eipl.amcs.reportengine.repository.RptReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportExecutionServiceImpl implements ReportExecutionService {

    private final RptReportRepository reportRepository;

    private final DatasourceExecutor datasourceExecutor;

    @Override
    public ReportResult execute(ReportRequest request) {

        RptReport report = reportRepository.findById(request.getReportId()).orElseThrow();

        return datasourceExecutor.execute(report.getDatasource(), request.getParameters());
    }
}