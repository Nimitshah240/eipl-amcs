package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.MemberCreditLimitTransaction;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface MemberCreditLimitTransactionRepository extends BaseRepository<MemberCreditLimitTransaction, String> {

	@Override
	@EntityGraph(attributePaths = {"society"})
	Optional<MemberCreditLimitTransaction> findById(String id);
}
