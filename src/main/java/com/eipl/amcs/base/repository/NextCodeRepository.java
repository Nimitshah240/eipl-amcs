package com.eipl.amcs.base.repository;

public interface NextCodeRepository {

	String getNextCode(String className, String pkColumnName, String prefix, int numberOfDigit);
}
