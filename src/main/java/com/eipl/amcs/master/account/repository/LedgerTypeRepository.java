package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.LedgerType;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface LedgerTypeRepository extends BaseRepository<LedgerType, Integer> {

	@Override
	List<LedgerType> findAll(Sort sort);

	@Override
	Optional<LedgerType> findById(Integer integer);
}
