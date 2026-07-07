package com.eipl.amcs.reportengine.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReportResult {

    private List<?> data = new ArrayList<>();

}