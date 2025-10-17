package com.eipl.amcs.base.service;

public interface NextCodeService {
    String getNextCode(String className, String pkColumnName, String prefix, int numberOfDigit);
}
