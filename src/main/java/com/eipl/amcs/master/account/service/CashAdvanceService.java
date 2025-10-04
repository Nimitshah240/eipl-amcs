package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.dto.CashAdvanceDto;
import com.eipl.amcs.master.account.model.CashAdvance;

import java.util.List;
import java.util.Optional;

public interface CashAdvanceService {
    List<CashAdvance> findAll();

    CashAdvance save(CashAdvanceDto cashAdvance, String identityInfo);

    CashAdvance update(CashAdvanceDto cashAdvance, String identityInfo);

    Optional<CashAdvance> findById(String cashAdvanceNo);

    void delete(String cashAdvanceNo, String identityInfo);

    void delete(CashAdvanceDto cashAdvance, String identityInfo);
}
