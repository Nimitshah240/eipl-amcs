package com.eipl.amcs.reportengine.service;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface LookupService {
    List<T> load(Long lookupId);
}
