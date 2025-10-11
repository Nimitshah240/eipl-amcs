package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.dto.YearClosingDto;
import com.eipl.amcs.master.account.model.FinancialYear;

import java.util.List;

public interface FinancialYearService {

    List<FinancialYear> findAll();

    YearClosingDto saveDto(YearClosingDto dto, String identityHeader);
}
