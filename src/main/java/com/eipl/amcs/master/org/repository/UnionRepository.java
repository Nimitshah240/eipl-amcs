package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Union;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnionRepository extends BaseRepository<Union, String> {

	@Override
	@EntityGraph(attributePaths = { "bank", "branch", "state", "district", "subDistrict", "village", "hamlet" })
	List<Union> findAll(Sort sort);

	@Override
	@EntityGraph(attributePaths = { "bank", "branch", "state", "district", "subDistrict", "village", "hamlet" })
	Optional<Union> findById(String code);
}
