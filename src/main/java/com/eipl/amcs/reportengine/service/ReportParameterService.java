package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.StaticLookupItem;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.model.RptReportParameter;
import com.eipl.amcs.reportengine.repository.RptParameterRepository;
import com.eipl.amcs.reportengine.repository.RptReportParameterRepository;
import com.eipl.amcs.reportengine.util.ReflectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportParameterService {

    private final RptReportParameterRepository repository;
    private final RptParameterRepository reportParameterRepository;


    public List<RptReportParameter> getParameters(Long reportId) {
        return reportParameterRepository.findByReportReportCodeOrderByDisplayOrderAsc(reportId);
    }


    public void saveDefaults(Long reportId, Map<String, Object> values) {

        List<RptReportParameter> parameters = repository.findByReportReportCodeOrderByDisplayOrder(reportId);

        for (RptReportParameter rp : parameters) {

            RptParameterMaster pm = rp.getParameterMaster();

            if (pm == null) {
                continue;
            }

            Object value = values.get(pm.getParameterMasterCode());

            if (value == null) {
                rp.setDefaultValue(null);
            } else if (value instanceof StaticLookupItem) {
                rp.setDefaultValue(((StaticLookupItem) value).getValue());
            } else if (pm.getLookup() != null) {
                Object lookupValue = ReflectionUtil.getFieldValue(
                        value,
                        pm.getLookup().getValueField());

                rp.setDefaultValue(
                        lookupValue == null ? null : lookupValue.toString());
            } else {
                rp.setDefaultValue(value.toString());
            }
        }

        repository.saveAll(parameters);
    }
}