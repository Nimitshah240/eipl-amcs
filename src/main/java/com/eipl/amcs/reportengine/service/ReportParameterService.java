package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.model.RptReportParameter;
import com.eipl.amcs.reportengine.repository.RptParameterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportParameterService {

    private final RptParameterRepository reportParameterRepository;

    public List<RptReportParameter> getParameters(Long reportId) {
        return reportParameterRepository.findByReportReportCodeOrderByDisplayOrderAsc(reportId);
    }
}