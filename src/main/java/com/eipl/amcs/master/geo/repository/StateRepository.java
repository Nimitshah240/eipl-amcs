package com.eipl.amcs.master.geo.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.geo.model.State;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface StateRepository extends BaseRepository<State, String> {

	@Override
	List<State> findAll(Sort sort);

}
