package com.eipl.amcs.reportengine.service;


import java.util.List;

public interface LookupService {
    <T> List<T> load(String lookupId);
}
