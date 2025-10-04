package com.eipl.amcs.master.geo.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.State;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends BaseRepository<District, String> {

	@Override
	@EntityGraph(attributePaths = { "state" })
	List<District> findAll(Sort sort);

	@EntityGraph(attributePaths = { "state" })
	List<District> findByState(State state, Sort sort);
}
