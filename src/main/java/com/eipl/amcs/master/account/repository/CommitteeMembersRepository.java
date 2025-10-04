package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.CommitteeMembers;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;


public interface CommitteeMembersRepository extends BaseRepository<CommitteeMembers, String> {

	@Override
	@EntityGraph(attributePaths = { "society","designation"})
	List<CommitteeMembers> findAll(Sort sort);

	@Override
	@EntityGraph(attributePaths = { "society","designation"})
	Optional<CommitteeMembers> findById(String s);
}
