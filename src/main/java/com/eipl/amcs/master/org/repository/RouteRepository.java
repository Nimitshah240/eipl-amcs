package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Route;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends BaseRepository<Route, String> {

	@Override
	@EntityGraph(attributePaths = { "union", "bmc" })
	List<Route> findAll(Sort sort);

	@Override
	@EntityGraph(attributePaths = { "union", "bmc" })
	Optional<Route> findById(String id);
}
