package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.SocietyYearClosing;

import java.util.List;

public interface SocietyYearClosingService {
    List<SocietyYearClosing> findAll();

    SocietyYearClosing save(SocietyYearClosing societyYearClosing, String identityInfo);
}
