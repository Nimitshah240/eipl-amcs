package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.VoucherType;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface VoucherTypeRepository extends BaseRepository<VoucherType, Integer> {

	@Override
	List<VoucherType> findAll(Sort sort);


	@Override
	Optional<VoucherType> findById(Integer integer);
}
