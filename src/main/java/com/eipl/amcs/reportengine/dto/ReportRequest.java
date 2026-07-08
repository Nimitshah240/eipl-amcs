package com.eipl.amcs.reportengine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    private Long reportId;
    private Map<String, Object> parameters;
}