package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.repository.TaxDetailRepository;
import com.eipl.amcs.master.account.repository.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaxServiceImpl implements TaxService {

    @Autowired
    TaxRepository taxRepository;
    @Autowired
    TaxDetailRepository taxDetailRepository;

    @Override
    public List<TaxDto> findAll() {
        List<TaxDto> list = new ArrayList<>();
        List<Tax> taxes = taxRepository.findAll();
        taxes.forEach(item -> {
            TaxDto dto = new TaxDto();
            dto.setTax(item);
            dto.setTaxDetails(taxDetailRepository.findByTax(item));
            list.add(dto);
        });
        return list;
    }
}
