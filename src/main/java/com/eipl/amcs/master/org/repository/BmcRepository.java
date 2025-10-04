package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Bmc;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface BmcRepository extends BaseRepository<Bmc, String> {

	@Override
	@EntityGraph(attributePaths = { "union", "mcc", "state", "district", "subDistrict", "village", "hamlet" })
	List<Bmc> findAll(Sort sort);

	@Override
	@EntityGraph(attributePaths = { "union", "mcc", "state", "district", "subDistrict", "village", "hamlet" })
	Optional<Bmc> findById(String id);
}
