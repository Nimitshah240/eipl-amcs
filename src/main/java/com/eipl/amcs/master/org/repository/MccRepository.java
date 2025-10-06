package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Mcc;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MccRepository extends BaseRepository<Mcc, String> {

	@Override
	@EntityGraph(attributePaths = { "union", "plant", "state", "district", "subDistrict", "village", "hamlet" })
	List<Mcc> findAll(Sort sort);

	@Override
	@EntityGraph(attributePaths = { "union", "plant", "state", "district", "subDistrict", "village", "hamlet" })
	Optional<Mcc> findById(String id);

}
