package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.model.RptTableResult;

import java.util.List;

public interface RptTableResultService {

    List<RptTableResult> findAll();

    List<RptTableResult> findByReportCode(Long reportCode);

    List<RptTableResult> save(List<RptTableResult> rptTableResultList);

}