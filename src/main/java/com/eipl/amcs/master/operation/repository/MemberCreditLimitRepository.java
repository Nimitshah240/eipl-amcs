package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.MemberCreditLimit;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface MemberCreditLimitRepository extends BaseRepository<MemberCreditLimit, String> {
	
	@EntityGraph(attributePaths = { "society" })
	Optional<MemberCreditLimit> findByConsumerCodeAndConsumerType(String code, Short type);

	@Override
	@EntityGraph(attributePaths = { "society" })
	Optional<MemberCreditLimit> findById(String id);
}
