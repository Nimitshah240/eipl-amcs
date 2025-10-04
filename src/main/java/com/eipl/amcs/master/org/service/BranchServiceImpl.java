package com.eipl.amcs.master.org.service;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.eipl.amcs.master.org.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.bankRepository;
import static com.eipl.amcs.config.BeanConfig.branchRepository;

@Service
public class BranchServiceImpl implements BranchService {
//	@Autowired
//	private BranchRepository branchRepository;
//	@Autowired
//	private BankRepository bankRepository;

	@Override
	public List<Branch> findAll() {
		return branchRepository.findAll(Sort.by("name"));
	}

	@Override
	public List<Branch> findAll(String bankCode) {
		Bank bank = bankRepository.findById(bankCode)
				.orElseThrow(() -> new EntityNotFoundException(Bank.class, "invalid.bank"));
		return branchRepository.findByBank(bank, Sort.by("name"));
	}
}
