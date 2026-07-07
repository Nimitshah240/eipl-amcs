package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.reportengine.dto.StaticLookupItem;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.util.ReflectionUtil;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ParameterResolver {

    public Object resolve(RptParameterMaster parameter, Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return value;
        }

        if (value instanceof Number) {
            return value;
        }

        if (value instanceof Boolean) {
            return value;
        }

        if (value instanceof Timestamp) {
            return value;
        }

        if (value instanceof LocalDateTime) {
            return value;
        }

        if (value instanceof LocalDate) {
            return Timestamp.valueOf(((LocalDate) value).atStartOfDay());
        }

        if (value instanceof StaticLookupItem) {
            return ((StaticLookupItem) value).getValue();
        }

        if (parameter.getLookup() != null) {

            String valueField = parameter.getLookup().getValueField();

            return ReflectionUtil.getFieldValue(value, valueField);
        }

        return value;
    }

}