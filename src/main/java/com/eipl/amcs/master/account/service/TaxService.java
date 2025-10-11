package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.dto.TaxDto;

import java.util.List;

public interface TaxService {
    List<TaxDto> findAll();
}
