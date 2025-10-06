package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends BaseRepository<Branch, String> {

	@Override
	@EntityGraph(attributePaths = { "bank", "state", "district", "subDistrict", "village" })
	List<Branch> findAll(Sort sort);

	@EntityGraph(attributePaths = { "bank", "state", "district", "subDistrict", "village" })
	List<Branch> findByBank(Bank bank, Sort sort);

	@Override
	@EntityGraph(attributePaths = { "bank", "state", "district", "subDistrict", "village" })
	Optional<Branch> findById(String id);
}
