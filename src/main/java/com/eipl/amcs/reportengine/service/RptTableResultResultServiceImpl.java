package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.model.RptTableResult;
import com.eipl.amcs.reportengine.repository.RptTableResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RptTableResultResultServiceImpl implements RptTableResultService {

    @Autowired
    private final RptTableResultRepository rptTableResultRepository;


    @Override
    public List<RptTableResult> findAll() {
        return rptTableResultRepository.findAll();
    }

    @Override
    public List<RptTableResult> findByReportCode(Long reportCode) {
        return rptTableResultRepository.findByReportCode(reportCode, Sort.by("respSeq").ascending());
    }

    @Override
    public List<RptTableResult> save(List<RptTableResult> rptTableResultList) {
        return rptTableResultRepository.saveAll(rptTableResultList);
    }
}