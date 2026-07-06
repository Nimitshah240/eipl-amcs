package com.eipl.amcs.reportengine.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ReportResult {

    private List<Map<String, Object>> data = new ArrayList<>();

}