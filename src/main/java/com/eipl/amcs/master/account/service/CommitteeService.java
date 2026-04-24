package com.eipl.amcs.master.account.service;

public interface CommitteeService {

    String nextCode(String societyCode);

    Boolean deleteById(String code);
}
