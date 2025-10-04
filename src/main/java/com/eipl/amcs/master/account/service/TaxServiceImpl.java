package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.taxDetailRepository;
import static com.eipl.amcs.config.BeanConfig.taxRepository;

@Service
public class TaxServiceImpl implements TaxService {

/*	TaxRepository taxRepository;
	TaxDetailRepository taxDetailRepository;*/


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
