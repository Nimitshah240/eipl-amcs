package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Bank;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends BaseRepository<Bank, String> {

	@Override
	List<Bank> findAll(Sort sort);
	
	@Override
	Optional<Bank> findById(String id);

}
